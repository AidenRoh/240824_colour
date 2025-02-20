package com.colour.board.api.likes.controlelr;

import com.colour.board.api.likes.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/likePost")
    public ResponseEntity<String> likePost(@RequestParam long postId) {
        Long memberId = getCurrentMemberId();
        likesService.likePost(postId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body("like this post");
    }

    @DeleteMapping("/dislikePost")
    public ResponseEntity<String> dislikePost(@RequestParam long postId) {
        Long memberId = getCurrentMemberId();
        likesService.dislikePost(postId, memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("unlike this post");
    }
}
