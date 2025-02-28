package com.colour.board.api.comment.repository;

import com.colour.board.api.comment.domain.dto.ResponseDto;
import com.colour.board.api.comment.domain.entity.Response;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResponseRepository {

    Response save(Response response);

    Optional<Response> findById(Long responseId);

    List<Response> findAll(ResponseDto cond);

    void update(Long responseId, ResponseDto dto);

    void delete(Long responseId);
}
