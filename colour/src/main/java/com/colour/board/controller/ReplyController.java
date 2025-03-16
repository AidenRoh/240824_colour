package com.colour.board.controller;

import com.colour.board.api.comment.domain.dto.ResponseDto;
import com.colour.board.api.comment.domain.entity.Reply;
import com.colour.board.api.comment.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ResponseService replyService;

    @GetMapping("/create")
    public ResponseEntity<String> createReply() {
        return ResponseEntity.status(HttpStatus.OK).body("create-reply");
    }

    @GetMapping("/update")
    public ResponseEntity<String> updateReply() {
        return ResponseEntity.status(HttpStatus.OK).body("update-reply");
    }

    @PostMapping("/{commentId}/create")
    public ResponseEntity<String> createReply(@RequestBody ResponseDto dto,
                                              @PathVariable Long commentId) {
        Reply reply = new Reply(commentId, getCurrentMemberId(), dto.getContent());
        replyService.create(reply);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @PatchMapping("/{replyId}/update")
    @PreAuthorize("@replyOwnerValidator.validateReplyOwner(#replyId)")
    public ResponseEntity<String> updateReply(@RequestBody ResponseDto dto,
                                              @PathVariable Long replyId) {
        replyService.update(replyId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @DeleteMapping("/{replyId}/delete")
    @PreAuthorize("@replyOwnerValidator.validateReplyOwner(#replyId)")
    public ResponseEntity<String> deleteComment(@PathVariable Long replyId) {
        replyService.delete(replyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("reply-deleted");
    }
}
