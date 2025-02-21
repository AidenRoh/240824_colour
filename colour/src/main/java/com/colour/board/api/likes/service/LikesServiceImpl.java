package com.colour.board.api.likes.service;

import com.colour.board.api.likes.domain.dto.LikesDto;
import com.colour.board.api.likes.domain.entity.Likes;
import com.colour.board.api.likes.repository.LikesRepository;
import com.colour.board.api.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikesServiceImpl implements LikesService {

    private final LikesRepository likesRepository;
    private final PostRepository postRepository;

    public LikesServiceImpl(LikesRepository repository, PostRepository postRepository) {
        this.likesRepository = repository;
        this.postRepository = postRepository;
    }

    @Override
    public void likePost(Likes likes) {
        likesRepository.save(likes);
        postRepository.likePost(likes.getPostId());
    }

    @Override
    public void dislikePost(LikesDto dto) {
        likesRepository.delete(dto.getPostId(), dto.getMemberId());
        postRepository.dislikePost(dto.getPostId());
    }

    @Override
    public boolean isLikeExist(LikesDto dto) {
        return likesRepository.existsByKeys(dto.getPostId(), dto.getMemberId());
    }
}
