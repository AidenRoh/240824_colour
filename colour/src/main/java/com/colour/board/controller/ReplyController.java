package com.colour.board.controller;

import com.colour.board.api.comment.domain.dto.ResponseRegisterDto;
import com.colour.board.api.comment.domain.dto.ResponseUpdateDto;
import com.colour.board.api.comment.domain.entity.Comment;
import com.colour.board.api.comment.domain.entity.Reply;
import com.colour.board.api.comment.service.ResponseService;
import com.colour.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ResponseService replyService;
    private final MemberService memberService;

    @GetMapping("/create")
    public ResponseEntity<String> createReply() {
        return ResponseEntity.status(HttpStatus.OK).body("create-reply");
    }

    @GetMapping("/update")
    public ResponseEntity<String> updateReply() {
        return ResponseEntity.status(HttpStatus.OK).body("update-reply");
    }

    @PostMapping("/{commentId}/create")
    public ResponseEntity<String> createReply(@RequestBody ResponseRegisterDto dto,
                                              @PathVariable Long commentId) {
        Reply reply = new Reply(commentId, getCurrentMemberId(), dto.getContent());
        replyService.save(reply);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @PatchMapping("/{replyId}/update")
    public ResponseEntity<String> updateReply(@RequestBody ResponseUpdateDto dto,
                                              @PathVariable Long replyId) {
        replyService.update(getCurrentMemberId(), replyId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    //TODO: Search Logic needs to be revised
    @GetMapping("/get_comments")
    public List<Comment> getReplies() {
        return List.of();
    }

    @DeleteMapping("/{replyId}/delete")
    public ResponseEntity<String> deleteComment(@PathVariable Long replyId) {
        replyService.delete(getCurrentMemberId(), replyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("reply-deleted");
    }
}
