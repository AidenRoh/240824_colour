package com.colour.board.controller;

import com.colour.board.facade.BoardFacadeService;
import com.colour.board.post.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("board/test")
@RequiredArgsConstructor
public class BoardController {

    private final BoardFacadeService service;

    //TODO: search 관련 로직 구현해야함

    @GetMapping("boardCreate")
    public String boardCreate() {
        return "board create";
    }

    @GetMapping("boardUpdate")
    public String boardUpdate() {
        return "board update";
    }

    @PostMapping("boardCreate/{memberId}")
    public String boardCreate(@RequestBody PostDto dto, @PathVariable Long memberId) {
        service.createBoard(dto, memberId);
        return "board created - done";
    }

    @PatchMapping("boardUpdate/{postId}/{memberId}")
    public String boardUpdate(@RequestBody PostDto dto, @PathVariable Long postId, @PathVariable Long memberId) {
        service.updateBoard(dto, postId, memberId);
        return "board updated - done";
    }

    @DeleteMapping("boardDelete/{postId}")
    public String boardDelete(@PathVariable Long postId) {
        service.deleteBoard(postId);
        return "board deleted - done";
    }

    @GetMapping("search")
    public String search(Long hashtagId) {
        service.findByHashtagId(hashtagId);
        return "search";
    }
}
