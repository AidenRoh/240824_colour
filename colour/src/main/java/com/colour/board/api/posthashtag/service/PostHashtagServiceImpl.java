package com.colour.board.api.posthashtag.service;

import com.colour.board.api.posthashtag.dto.PostHashtagDto;
import com.colour.board.api.posthashtag.entity.PostHashtag;
import com.colour.board.api.posthashtag.repository.PostHashtagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostHashtagServiceImpl implements PostHashtagService {

    private final PostHashtagRepository repository;

    public PostHashtagServiceImpl(PostHashtagRepository repository) {
        this.repository = repository;
    }

    @Override
    public PostHashtag save(PostHashtag postHashtag) {
        return repository.save(postHashtag);
    }

    @Override
    public List<PostHashtag> findByCond(PostHashtagDto dto) {
        return repository.findByCond(dto);
    }

    @Override
    public void delete(Long postHashtagId) {
        repository.delete(postHashtagId);
    }

    @Override
    public void delete(Long postId, Long memberId) {
        repository.delete(postId, memberId);
    }
}
