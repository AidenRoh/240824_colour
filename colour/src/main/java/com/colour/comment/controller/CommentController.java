package com.colour.comment.controller;

import com.colour.comment.dto.ResponseRegisterDto;
import com.colour.comment.dto.ResponseUpdateDto;
import com.colour.comment.entity.Comment;
import com.colour.comment.entity.Response;
import com.colour.comment.service.ResponseService;
import com.colour.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("comment/test")
@RequiredArgsConstructor
public class CommentController {

    private final ResponseService commentService;
    private final MemberService memberService;

    @GetMapping("create-comment")
    public String createComment() {
        return "create-comment";
    }

    @PostMapping("create-comment/{board_id}/{member_id}")
    public String createComment(@RequestBody ResponseRegisterDto dto,
                                @PathVariable Long board_id, @PathVariable Long member_id) {
        String writer = memberService.findMemberById(member_id).getUsername();
        Comment comment = new Comment(board_id, member_id, writer, dto.getComment());
        commentService.save(comment);
        return "ok";
    }

    @GetMapping("update-comment")
    public String updateComment() {
        return "update-comment";
    }

    @PatchMapping("update-comment/{member_id}/{comment_id}")
    public String updateComment(@RequestBody ResponseUpdateDto dto,
                                @PathVariable Long member_id, @PathVariable Long comment_id) {
        if (doesWriterRequest(member_id, comment_id)) {
            commentService.update(comment_id, dto);
        }
        return "ok";
    }

    @GetMapping("get_comments")
    public List<Comment> getComments() {
        return List.of();
    }

    @DeleteMapping("delete-comment/{member_id}/{comment_id}")
    public String deleteComment(@PathVariable Long member_id, @PathVariable Long comment_id) {
        if (doesWriterRequest(member_id, comment_id)) {
            commentService.delete(comment_id);
        }
        return "ok";
    }

    private boolean doesWriterRequest(Long member_id, Long comment_id) {
        Response comment = commentService.findResponseById(comment_id);
        return Objects.equals(((Comment) comment).getMemberId(), member_id);
    }
}
