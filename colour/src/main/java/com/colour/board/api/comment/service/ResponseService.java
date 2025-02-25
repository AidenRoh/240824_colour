package com.colour.board.api.comment.service;

import com.colour.board.api.comment.domain.dto.ResponseSearchCond;
import com.colour.board.api.comment.domain.dto.ResponseUpdateDto;
import com.colour.board.api.comment.domain.entity.Response;

import java.util.List;

public interface ResponseService {

    Response save(Response response);

    Response findResponseById(Long responseId);

    List<Response> findAll(ResponseSearchCond cond);

    void update(Long memberId, Long responseId, ResponseUpdateDto dto);

    void delete(Long memberId, Long responseId);
}
