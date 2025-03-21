package com.colour.search.controller;

import com.colour.board.api.colorpalette.domain.dto.ColorCond;
import com.colour.board.api.colorpalette.service.AbstractColorService;
import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.post.domain.dto.PostResponseDto;
import com.colour.search.domain.dto.SearchDto;
import com.colour.search.domain.enums.SortOption;
import com.colour.search.facade.SearchFacadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.colour.search.domain.enums.SortOption.RELEVANCE;

@RestController
@RequestMapping("search")
public class SearchController {

    private final SearchFacadeService service;

    public SearchController(SearchFacadeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> searchPost(@RequestParam(required = false) String q,
                                                            @RequestParam(required = false) String sort,
                                                            @RequestBody SearchDto searchDto,
                                                            Pageable pageable) throws IOException {
        Pageable sortPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                SortOption.getSort((sort != null) ? sort : RELEVANCE.name()));

        // SearchDto 에 대해 front 는 값이 없더라고 '[]' 로 백엔드로 넘겨야함 (emptyList()) 로 처리하기 위해
        List<HashtagDto> hashtagList = new ArrayList<>(searchDto.getHashtags());
        List<ColorCond> colorCondList = new ArrayList<>();
        colorCondList.addAll(searchDto.getHexColors().stream()
                .map(AbstractColorService::getColorCond)
                .toList());
        colorCondList.addAll(searchDto.getColorConds());

        if (q != null) {
            Page<PostResponseDto> postResponseDtos = service.doElasticSearch(q, colorCondList, hashtagList, sortPageable);
            return ResponseEntity.ok(postResponseDtos);
        } else {
            Page<PostResponseDto> postResponseDtos = service.doFilterSearch(colorCondList, hashtagList, sortPageable);
            return ResponseEntity.ok(postResponseDtos);
        }

    }
}
