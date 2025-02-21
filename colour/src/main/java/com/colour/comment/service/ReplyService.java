package com.colour.comment.service;

import com.colour.comment.domain.dto.ResponseSearchCond;
import com.colour.comment.domain.dto.ResponseUpdateDto;
import com.colour.comment.domain.entity.Comment;
import com.colour.comment.domain.entity.Response;
import com.colour.comment.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ReplyService implements ResponseService {

    private final ResponseRepository repository;

    public ReplyService(@Qualifier("replyRepository") ResponseRepository repository) {
        this.repository = repository;
    }

    @Override
    public Response save(Response reply) {
        return repository.save(reply);
    }

    @Override
    public Response findResponseById(Long replyId) {
        return repository.findById(replyId).orElse(null);
    }

    @Override
    public List<Response> findAll(ResponseSearchCond cond) {
        return List.of();
    }

    @Override
    public void update(Long memberId, Long responseId, ResponseUpdateDto dto) {
        if (doesWriterRequest(memberId, responseId)) {
            repository.update(responseId, dto);
        }
    }

    @Override
    public void delete(Long memberId, Long responseId) {
        if (doesWriterRequest(memberId, responseId)) {
            repository.delete(responseId);
        }
    }

    private boolean doesWriterRequest(Long memberId, Long commentId) {
        Response comment = findResponseById(commentId);
        return Objects.equals(((Comment) comment).getMemberId(), memberId);
    }
}
