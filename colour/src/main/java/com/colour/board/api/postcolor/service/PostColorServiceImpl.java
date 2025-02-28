package com.colour.board.api.postcolor.service;

import com.colour.board.api.postcolor.domain.dto.PostColorDto;
import com.colour.board.api.postcolor.domain.entity.PostColor;
import com.colour.board.api.postcolor.repository.PostColorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostColorServiceImpl implements PostColorService {

    private final PostColorRepository repository;

    public PostColorServiceImpl(PostColorRepository repository) {
        this.repository = repository;
    }

    @Override
    public void create(PostColor postColor) {
        repository.save(postColor);
    }

    @Override
    public List<PostColor> findByCond(PostColorDto dto) {
        return repository.findByCond(dto);
    }

    @Override
    public void delete(Long postId, Long colorId) {
        repository.delete(postId, colorId);
    }

    @Override
    public boolean isPostColorExist(Long postId, Long colorId) {
        return repository.existsByKeys(postId, colorId);
    }
}
