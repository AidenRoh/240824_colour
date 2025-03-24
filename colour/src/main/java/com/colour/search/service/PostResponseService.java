package com.colour.search.service;

import com.colour.board.api.colorpalette.domain.dto.ColorDto;
import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.post.domain.dto.PostResponseDto;
import com.colour.search.repository.PostResponseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.colour.search.domain.enums.SortOption.RELEVANCE;

@Service
@Transactional(readOnly = true)
public class PostResponseService {

    private final PostResponseRepository repository;

    public PostResponseService(PostResponseRepository repository) {
        this.repository = repository;
    }

    public Page<PostResponseDto> createFilterResponse(List<Long> postIds, Pageable pageable, Long total) {
        if (postIds.isEmpty()) {
            return Page.empty();
        }
        manipulateListBySort(postIds, pageable);
        List<PostResponseDto> searchResponse = asyncSearchAndUnion(postIds, pageable);
        return new PageImpl<>(searchResponse, pageable, total);
    }

    public Page<PostResponseDto> createElasticResponse(List<Long> postIds, Pageable pageable, Long total) {
        List<PostResponseDto> searchResponse = asyncSearchAndUnion(postIds, pageable);
        return new PageImpl<>(searchResponse, pageable, total);
    }

    private List<PostResponseDto> asyncSearchAndUnion(List<Long> postId, Pageable pageable) {
        CompletableFuture<List<PostResponseDto>> basicFuture = CompletableFuture.supplyAsync(() ->
                repository.getResponses(postId, pageable));
        CompletableFuture<Map<Long, List<ColorDto>>> colorFuture = CompletableFuture.supplyAsync(() ->
                repository.getColorDtos(postId, pageable));
        CompletableFuture<Map<Long, List<HashtagDto>>> hashtagFuture = CompletableFuture.supplyAsync(() ->
                repository.getHashtagDtos(postId, pageable));

        try {
            CompletableFuture.allOf(basicFuture, colorFuture, hashtagFuture).join();
            List<PostResponseDto> searchResponse = basicFuture.get();
            Map<Long, List<ColorDto>> colorMap = colorFuture.get();
            Map<Long, List<HashtagDto>> hashtagMap = hashtagFuture.get();

            for (PostResponseDto each : searchResponse) {
                each.setColors(colorMap.getOrDefault(each.getPostId(), Collections.emptyList()));
                each.setHashtags(hashtagMap.getOrDefault(each.getPostId(), Collections.emptyList()));
            }
            return searchResponse;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("search response 실행 중 에러");
            return Collections.emptyList();
        }

    }

    private void manipulateListBySort(List<Long> postIds, Pageable pageable) {
        Sort sort = pageable.getSort();
        boolean isRelevance = sort.stream()
                .findFirst()
                .map(order -> order.getProperty().equals(RELEVANCE.getField()))
                .orElse(false);
        if (!sort.isEmpty() && isRelevance) {
            int fromIndex = (int) pageable.getOffset();
            int toIndex = (int) pageable.getOffset() + pageable.getPageSize();
            toIndex = Math.min(toIndex, postIds.size());
            postIds.subList(fromIndex, toIndex);
        }
    }
}
