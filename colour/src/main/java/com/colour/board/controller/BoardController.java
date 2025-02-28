package com.colour.board.controller;

import com.colour.board.api.likes.domain.dto.LikesDto;
import com.colour.board.api.post.domain.dto.PostDto;
import com.colour.board.api.post.domain.entity.Post;
import com.colour.board.facade.BoardFacadeService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<String> createBoard(HttpServletRequest request) {
        Post tempBoard = service.createTempBoard(getCurrentMemberId());
        request.getSession().setAttribute("board", tempBoard.getPostId());
        return ResponseEntity.status(HttpStatus.OK).body("temporary board created");
    }

    @GetMapping("/update")
    public ResponseEntity<String> updateBoard() {
        return ResponseEntity.status(HttpStatus.OK).body("board updated");
    }

    @PutMapping("/create")
    public ResponseEntity<String> createBoard(@RequestBody PostDto dto, HttpServletRequest request) {
        Long postId = (Long) request.getSession().getAttribute("board");
        service.createBoard(dto, postId);
        return ResponseEntity.status(HttpStatus.CREATED).body("board created - done");
    }

    @PatchMapping("/{boardId}/update")
    public ResponseEntity<String> updateBoard(@RequestBody PostDto dto, @PathVariable Long boardId) {
        service.updateBoard(dto, boardId);
        return ResponseEntity.status(HttpStatus.OK).body("board updated - done");
    }

    @DeleteMapping("/{boardId}/delete")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
        service.deleteBoard(boardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("board deleted - done");
    }

    @PostMapping("/{boardId}/likePost")
    public ResponseEntity<String> likePost(@PathVariable Long boardId) {
        LikesDto dto = LikesDto.builder().postId(boardId).memberId(getCurrentMemberId()).build();
        service.likePost(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("like this post");
    }

    @DeleteMapping("/{boardId}/dislikePost")
    public ResponseEntity<String> dislikePost(@PathVariable Long boardId) {
        LikesDto dto = LikesDto.builder().postId(boardId).memberId(getCurrentMemberId()).build();
        service.dislikePost(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("unlike this post");
    }

//    @GetMapping("/search")
//    public String search(Long hashtagId) {
//        service.findByHashtagId(hashtagId);
//        return "search";
//    }

}
