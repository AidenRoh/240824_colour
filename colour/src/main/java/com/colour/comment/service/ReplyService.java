package com.colour.comment.service;

import com.colour.comment.dto.ResponseSearchCond;
import com.colour.comment.dto.ResponseUpdateDto;
import com.colour.comment.entity.Response;
import com.colour.comment.repository.ResponseRepository;

import java.util.List;

public class ReplyService implements ResponseService {

    private final ResponseRepository repository;

    public ReplyService(ResponseRepository repository) {
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
    public void update(Long replyId, ResponseUpdateDto dto) {
        repository.update(replyId, dto);
    }

    @Override
    public void delete(Long replyId) {
        repository.delete(replyId);
    }
}
