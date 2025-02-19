package com.colour.board.controller;

import com.colour.board.facade.BoardFacadeService;
import com.colour.board.post.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardFacadeService service;

    //TODO: search 관련 로직 구현해야함

    @GetMapping("/create")
    public ResponseEntity<String> createBoard() {
        return ResponseEntity.status(HttpStatus.OK).body("board created");
    }

    @GetMapping("/update")
    public ResponseEntity<String> updateBoard() {
        return ResponseEntity.status(HttpStatus.OK).body("board updated");
    }

    @PostMapping("/create")
    public ResponseEntity<String> createBoard(@RequestBody PostDto dto) {
        service.createBoard(dto, getCurrentMemberId());
        return ResponseEntity.status(HttpStatus.CREATED).body("board created - done");
    }

    @PatchMapping("/{boardId}/update")
    public ResponseEntity<String> updateBoard(@RequestBody PostDto dto, @PathVariable Long boardId) {
        service.updateBoard(dto, boardId, getCurrentMemberId());
        return ResponseEntity.status(HttpStatus.OK).body("board updated - done");
    }

    @DeleteMapping("/{boardId}/delete")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
        service.deleteBoard(boardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("board deleted - done");
    }

    @GetMapping("/search")
    public String search(Long hashtagId) {
        service.findByHashtagId(hashtagId);
        return "search";
    }

}
