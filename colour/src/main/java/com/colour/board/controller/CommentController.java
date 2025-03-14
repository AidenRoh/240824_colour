package com.colour.board.controller;

import com.colour.board.api.comment.domain.dto.ResponseDto;
import com.colour.board.api.comment.domain.entity.Comment;
import com.colour.board.api.comment.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final ResponseService commentService;

    @GetMapping("/create")
    public ResponseEntity<String> createComment() {
        return ResponseEntity.status(HttpStatus.OK).body("create-comment");
    }

    @GetMapping("/update")
    public ResponseEntity<String> updateComment() {
        return ResponseEntity.status(HttpStatus.OK).body("update-comment");
    }

    @PostMapping("/{boardId}/create")
    public ResponseEntity<String> createComment(@RequestBody ResponseDto dto,
                                                @PathVariable Long boardId) {
        Comment comment = new Comment(boardId, getCurrentMemberId(), dto.getContent());
        commentService.create(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @PatchMapping("/{commentId}/update")
    @PreAuthorize("@commentOwnerValidator.validateCommentOwner(#commentId)")
    public ResponseEntity<String> updateComment(@RequestBody ResponseDto dto,
                                                @PathVariable Long commentId) {
        commentService.update(commentId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @DeleteMapping("/{commentId}/delete")
    @PreAuthorize("@commentOwnerValidator.validateCommentOwner(#commentId)")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("delete-comment");
    }

}
