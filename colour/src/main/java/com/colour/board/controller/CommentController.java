package com.colour.board.controller;

import com.colour.board.api.comment.domain.dto.ResponseRegisterDto;
import com.colour.board.api.comment.domain.dto.ResponseUpdateDto;
import com.colour.board.api.comment.domain.entity.Comment;
import com.colour.board.api.comment.service.ResponseService;
import com.colour.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final ResponseService commentService;
    private final MemberService memberService;

    @GetMapping("/create")
    public ResponseEntity<String> createComment() {
        return ResponseEntity.status(HttpStatus.OK).body("create-comment");
    }

    @GetMapping("/update")
    public ResponseEntity<String> updateComment() {
        return ResponseEntity.status(HttpStatus.OK).body("update-comment");
    }

    @PostMapping("/{boardId}/create")
    public ResponseEntity<String> createComment(@RequestBody ResponseRegisterDto dto,
                                                @PathVariable Long boardId) {
        Comment comment = new Comment(boardId, getCurrentMemberId(), dto.getContent());
        commentService.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @PatchMapping("/{commentId}/update")
    public ResponseEntity<String> updateComment(@RequestBody ResponseUpdateDto dto,
                                                @PathVariable Long commentId) {
        commentService.update(getCurrentMemberId(), commentId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @GetMapping("/getComments")
    public List<Comment> getComments() {
        return List.of();
    }

    @DeleteMapping("/{commentId}/delete")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.delete(getCurrentMemberId(), commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("delete-comment");
    }


}
