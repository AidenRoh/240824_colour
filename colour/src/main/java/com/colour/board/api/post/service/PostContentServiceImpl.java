package com.colour.board.api.post.service;

import com.colour.board.api.post.domain.dto.PostDto;
import com.colour.board.api.post.domain.entity.PostContent;
import com.colour.board.api.post.repository.PostContentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostContentServiceImpl implements PostContentService {

    private final PostContentRepository repository;

    public PostContentServiceImpl(PostContentRepository repository) {
        this.repository = repository;
    }

    @Override
    public PostContent createPostContent(PostContent postContent) {
        return repository.save(postContent);
    }

    @Override
    public void updatePostContent(Long post_id, PostDto postDto) {
        repository.update(post_id, postDto);
    }

    @Override
    public void deletePostContent(Long post_id) {
        repository.delete(post_id);
    }
}
