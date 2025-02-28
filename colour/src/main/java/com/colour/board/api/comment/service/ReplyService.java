package com.colour.board.api.comment.service;

import com.colour.board.api.comment.domain.dto.ResponseDto;
import com.colour.board.api.comment.domain.entity.Response;
import com.colour.board.api.comment.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReplyService implements ResponseService {

    private final ResponseRepository repository;

    public ReplyService(@Qualifier("replyJdbcRepository") ResponseRepository repository) {
        this.repository = repository;
    }

    @Override
    public Response create(Response reply) {
        return repository.save(reply);
    }

    @Override
    public Response findResponseById(Long replyId) {
        return repository.findById(replyId).orElse(null);
    }

    @Override
    public List<Response> findAll(ResponseDto cond) {
        return List.of();
    }

    @Override
    public void update(Long responseId, ResponseDto dto) {
        repository.update(responseId, dto);
    }

    @Override
    public void delete(Long responseId) {
        repository.delete(responseId);
    }

}
