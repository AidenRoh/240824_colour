package com.colour.board.api.posthashtag.service;

import com.colour.board.api.posthashtag.domain.dto.PostHashtagDto;
import com.colour.board.api.posthashtag.domain.entity.PostHashtag;
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
    public void create(PostHashtag postHashtag) {
        repository.save(postHashtag);
    }

    @Override
    public List<PostHashtag> findByCond(PostHashtagDto dto) {
        return repository.findByCond(dto);
    }

    @Override
    public void delete(long postId, long hashtagId) {
        repository.delete(postId, hashtagId);
    }

    @Override
    public boolean isPostHashtagExist(long postId, long hashtagId) {
        return repository.existsByKeys(postId, hashtagId);
    }
}
