package com.colour.board.controller;

import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.posthashtag.domain.dto.PostHashtagDto;
import com.colour.board.facade.HashtagFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;

@RestController
@RequestMapping("/hashtag")
@RequiredArgsConstructor
public class HashtagController {

    private final HashtagFacadeService service;

    @PostMapping("/{postId}/create")
    public ResponseEntity<String> createHashtag(@ModelAttribute HashtagDto dto,
                                                @PathVariable Long postId) {
        service.createHashtag(dto, postId, getCurrentMemberId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteHashtag(@ModelAttribute PostHashtagDto dto) {
        service.deleteHashtag(dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
