package com.colour.board.common.validator;

import com.colour.board.api.comment.domain.entity.Comment;
import com.colour.board.api.comment.repository.ResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;

@Component
@RequiredArgsConstructor
public class CommentOwnerValidator {

    @Qualifier("commentJdbcRepository")
    private final ResponseRepository repository;

    public boolean validateCommentOwner(Long commentId) {
        return repository.findById(commentId)
                .map(response -> (Comment) response)
                .map(comment -> comment.getMemberId().equals(getCurrentMemberId()))
                .orElse(false);
    }

}
