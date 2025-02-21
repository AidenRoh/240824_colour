package com.colour.board.api.post.service;

import com.colour.board.api.post.domain.dto.PostDto;
import com.colour.board.api.post.domain.entity.Post;
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
    public Post createPost(Post post) {
        return repository.save(post);
    }

    @Override
    public void updatePost(Long postId, PostDto dto, long memberId) {
        if (doesWriterRequest(postId, memberId)) {
            repository.update(postId, dto);
        }
    }

    @Override
    public void deletePost(Long postId, long memberId) {
        if (doesWriterRequest(postId, memberId)) {
            repository.delete(postId);
        }
    }

    @Override
    public Post findPostById(Long postId) {
        return repository.findById(postId).orElse(null);
    }

    @Override
    public List<Post> findPostByTitle(String title) {
        return repository.findByTitle(title);
    }

    private boolean doesWriterRequest(long postId, long memberId) {
        Post post = repository.findById(postId).orElse(null);
        assert post != null;
        return post.getMemberId() == memberId;
    }
}
