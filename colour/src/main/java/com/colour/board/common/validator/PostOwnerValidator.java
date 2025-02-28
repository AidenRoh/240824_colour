package com.colour.board.common.validator;

import com.colour.board.api.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;

@Component
@RequiredArgsConstructor
public class PostOwnerValidator {

    private final PostRepository repository;

    public boolean validatePostOwner(Long postId) {
        return repository.findById(postId)
                .map(post -> post.getMemberId().equals(getCurrentMemberId()))
                .orElse(false);
    }

    public boolean validatePostOwner(Long postId, Long memberId) {
        return repository.findById(postId)
                .map(post -> post.getMemberId().equals(memberId))
                .orElse(false);
    }
}
