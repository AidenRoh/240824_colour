package com.colour.comment.repository;

import com.colour.comment.dto.ResponseSearchCond;
import com.colour.comment.dto.ResponseUpdateDto;
import com.colour.comment.entity.Response;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResponseRepository {

    Response save(Response response);
    Optional<Response> findById(Long responseId);
    List<Response> findAll(ResponseSearchCond cond);
    void update(Long responseId, ResponseUpdateDto dto);
    void delete(Long responseId);
}
