package com.colour.comment.controller;

import com.colour.comment.dto.ResponseRegisterDto;
import com.colour.comment.dto.ResponseUpdateDto;
import com.colour.comment.entity.Comment;
import com.colour.comment.entity.Reply;
import com.colour.comment.entity.Response;
import com.colour.comment.service.ResponseService;
import com.colour.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("reply/test")
@RequiredArgsConstructor
public class ReplyController {

    private final ResponseService replyService;
    private final MemberService memberService;

    @GetMapping("create-reply")
    public String createReply() {
        return "create-reply";
    }

    @PostMapping("create-reply/{comment_id}/{member_id}")
    public String createReply(@RequestBody ResponseRegisterDto dto,
                                @PathVariable Long comment_id, @PathVariable Long member_id) {
        String writer = memberService.findMemberById(member_id).getUsername();
        Reply reply = new Reply(comment_id, member_id, writer, dto.getComment());
        replyService.save(reply);
        return "ok";
    }

    @GetMapping("update-reply")
    public String updateReply() {
        return "update-reply";
    }

    @PatchMapping("update-reply/{member_id}/{reply_id}")
    public String updateReply(@RequestBody ResponseUpdateDto dto,
                                @PathVariable Long member_id, @PathVariable Long reply_id) {
        if (doesWriterRequest(member_id, reply_id)) {
            replyService.update(reply_id, dto);
        }
        return "ok";
    }

    @GetMapping("get_comments")
    public List<Comment> getReplies() {
        return List.of();
    }

    @DeleteMapping("delete-reply/{member_id}/{reply_id}")
    public String deleteComment(@PathVariable Long member_id, @PathVariable Long reply_id) {
        if (doesWriterRequest(member_id, reply_id)) {
            replyService.delete(reply_id);
        }
        return "ok";
    }

    private boolean doesWriterRequest(Long member_id, Long reply_id) {
        Response reply = replyService.findResponseById(reply_id);
        return Objects.equals(((Reply) reply).getMemberId(), member_id);
    }
}
