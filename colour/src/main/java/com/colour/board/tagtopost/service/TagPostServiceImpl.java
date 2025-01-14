package com.colour.board.tagtopost.service;

import com.colour.board.tagtopost.dto.TagPostDto;
import com.colour.board.tagtopost.entity.TagPost;
import com.colour.board.tagtopost.repository.TagPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TagPostServiceImpl implements TagPostService {
    
    private final TagPostRepository repository;

    public TagPostServiceImpl(TagPostRepository repository) {
        this.repository = repository;
    }

    @Override
    public TagPost save(TagPost tagPost) {
        return repository.save(tagPost);
    }

    @Override
    public List<TagPost> findByCond(TagPostDto dto) {
        return repository.findByCond(dto);
    }

    @Override
    public void delete(Long tagPostId) {
        repository.delete(tagPostId);
    }

    @Override
    public void delete(Long postId, Long memberId) {
        repository.delete(postId, memberId);
    }
}
