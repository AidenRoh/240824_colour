package com.colour.board.common.validator;

import com.colour.board.api.comment.domain.entity.Comment;
import com.colour.board.api.comment.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;

@Component
public class CommentOwnerValidator {

    private final ResponseRepository repository;

    public CommentOwnerValidator(@Qualifier("commentJdbcRepository") ResponseRepository repository) {
        this.repository = repository;
    }

    public boolean validateCommentOwner(Long commentId) {
        return repository.findById(commentId)
                .map(response -> (Comment) response)
                .map(comment -> comment.getMemberId().equals(getCurrentMemberId()))
                .orElse(false);
    }

}
