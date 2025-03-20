package com.colour.board.controller;

import com.colour.board.api.colorpalette.domain.dto.ColorCond;
import com.colour.board.api.colorpalette.service.AbstractColorService;
import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.post.domain.dto.PostResponseDto;
import com.colour.board.api.search.domain.dto.SearchDto;
import com.colour.board.facade.SearchFacadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("search")
public class SearchController {

    private final SearchFacadeService service;

    public SearchController(SearchFacadeService service) {
        this.service = service;
    }

    public ResponseEntity<Page<PostResponseDto>> searchPost(@RequestParam(required = false) String keyword,
                                                            @RequestBody SearchDto searchDto,
                                                            Pageable pageable) {
        // SearchDto 에 대해 front 는 값이 없더라고 '[]' 로 백엔드로 넘겨야함 (emptyList()) 로 처리하기 위해
        List<HashtagDto> hashtagList = new ArrayList<>(searchDto.getHashtags());
        List<ColorCond> colorCondList = new ArrayList<>();
        colorCondList.addAll(searchDto.getHexColors().stream()
                .map(AbstractColorService::getColorCond)
                .toList());
        colorCondList.addAll(searchDto.getColorConds());

        if (keyword != null) {
            //service.doElasticSearch(keyword, colorCondList, hashtagList);
            // toss colorMatchScores and matchHashtag to elasticSearchService
            return null;
        } else {
            Page<PostResponseDto> postResponseDtos = service.doFilterSearch(colorCondList, hashtagList, pageable);
            return ResponseEntity.ok(postResponseDtos);
        }

    }
}
