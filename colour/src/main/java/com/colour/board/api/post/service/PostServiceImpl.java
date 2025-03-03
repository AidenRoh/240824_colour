package com.colour.board.api.post.service;

import com.colour.board.api.post.domain.dto.PostRequestDto;
import com.colour.board.api.post.domain.entity.Post;
import com.colour.board.api.post.domain.enums.PostStatus;
import com.colour.board.api.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository repository;

    public PostServiceImpl(PostRepository postRepository) {
        this.repository = postRepository;
    }

    @Override
    public Post createTemporaryPost(Post post) {
        return repository.save(post);
    }

    @Override
    public void updatePost(Long postId, PostRequestDto dto) {
        repository.update(postId, dto);
    }

    @Override
    public void deletePost(Long postId) {
        PostRequestDto dto = new PostRequestDto();
        dto.setStatus(PostStatus.DELETED.getStatus());
        repository.delete(postId, dto);
    }

    @Override
    public Post findPostById(Long postId) {
        return repository.findById(postId).orElse(null);
    }

    @Override
    public List<Post> findPostByTitle(String title) {
        return repository.findByTitle(title);
    }

}
