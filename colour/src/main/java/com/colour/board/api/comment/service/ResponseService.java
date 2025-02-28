package com.colour.board.api.comment.service;

import com.colour.board.api.comment.domain.dto.ResponseDto;
import com.colour.board.api.comment.domain.entity.Response;

import java.util.List;

public interface ResponseService {

    Response create(Response response);

    Response findResponseById(Long responseId);

    List<Response> findAll(ResponseDto cond);

    void update(Long responseId, ResponseDto dto);

    void delete(Long responseId);
}
