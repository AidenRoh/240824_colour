package com.colour.comment.service;

import com.colour.comment.dto.ResponseSearchCond;
import com.colour.comment.dto.ResponseUpdateDto;
import com.colour.comment.entity.Response;
import com.colour.comment.repository.ResponseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService implements ResponseService {

    private final ResponseRepository repository;

    public CommentService(ResponseRepository repository) {
        this.repository = repository;
    }

    @Override
    public Response save(Response comment) {
        return repository.save(comment);
    }

    @Override
    public Response findResponseById(Long commentId) {
        return repository.findById(commentId).orElse(null);
    }

    @Override
    public List<Response> findAll(ResponseSearchCond cond) {
        return List.of();
    }

    @Override
    public void update(Long commentId, ResponseUpdateDto dto) {
        repository.update(commentId, dto);
    }

    @Override
    public void delete(Long commentId) {
        repository.delete(commentId);
    }
}
