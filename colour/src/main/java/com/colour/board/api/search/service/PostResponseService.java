package com.colour.board.api.search.service;

import com.colour.board.api.colorpalette.domain.dto.ColorDto;
import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.post.domain.dto.PostResponseDto;
import com.colour.board.api.search.repository.PostResponseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class PostResponseService {

    private final PostResponseRepository repository;

    public PostResponseService(PostResponseRepository repository) {
        this.repository = repository;
    }

    public List<PostResponseDto> createResponse(List<Long> postIds) {
        List<PostResponseDto> searchResponse = repository.getResponses(postIds);
        Map<Long, List<ColorDto>> colorMap = repository.getColorDtos(postIds);
        Map<Long, List<HashtagDto>> hashtagMap = repository.getHashtagDtos(postIds);

        for (PostResponseDto each : searchResponse) {
            each.setColors(colorMap.getOrDefault(each.getPostId(), Collections.emptyList()));
            each.setHashtags(hashtagMap.getOrDefault(each.getPostId(), Collections.emptyList()));
        }
        return searchResponse;
    }
}
