package com.colour.board.api.likes.service;

import com.colour.board.api.post.repository.PostRepository;
import com.colour.board.api.likes.repository.LikesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikesServiceImpl implements LikesService {

    private final LikesRepository likeRepository;
    private final PostRepository postRepository;

    public LikesServiceImpl(LikesRepository repository, PostRepository postRepository) {
        this.likeRepository = repository;
        this.postRepository = postRepository;
    }

    @Override
    public void likePost(long postId, long memberId) {
        likeRepository.saveLike(postId, memberId);
        postRepository.likePost(postId);
    }

    @Override
    public void dislikePost(long postId, long memberId) {
        likeRepository.deleteLike(postId, memberId);
        postRepository.dislikePost(postId);
    }
}
