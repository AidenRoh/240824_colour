package com.colour.comment.service;

import com.colour.comment.dto.ResponseSearchCond;
import com.colour.comment.dto.ResponseUpdateDto;
import com.colour.comment.entity.Response;

import java.util.List;

public interface ResponseService {

    Response save(Response response);
    Response findResponseById(Long responseId);
    List<Response> findAll(ResponseSearchCond cond);
    void update(Long commentId, ResponseUpdateDto dto);
    void delete(Long commentId);
}
