package com.colour.comment.service;

import com.colour.comment.domain.dto.ResponseSearchCond;
import com.colour.comment.domain.dto.ResponseUpdateDto;
import com.colour.comment.domain.entity.Response;

import java.util.List;

public interface ResponseService {

    Response save(Response response);

    Response findResponseById(Long responseId);

    List<Response> findAll(ResponseSearchCond cond);

    void update(Long memberId, Long responseId, ResponseUpdateDto dto);

    void delete(Long memberId, Long responseId);
}
