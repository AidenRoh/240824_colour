package com.colour.board.common.validator;

import com.colour.board.api.comment.domain.entity.Reply;
import com.colour.board.api.comment.repository.ResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;

@Component
@RequiredArgsConstructor
public class ReplyOwnerValidator {

    @Qualifier("replyJdbcRepository")
    private final ResponseRepository repository;

    public boolean validateReplyOwner(Long replyId) {
        return repository.findById(replyId)
                .map(response -> (Reply) response)
                .map(comment -> comment.getMemberId().equals(getCurrentMemberId()))
                .orElse(false);
    }
}
