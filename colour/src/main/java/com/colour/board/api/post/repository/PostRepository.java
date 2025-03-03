package com.colour.board.api.post.repository;

import com.colour.board.api.post.domain.dto.PostRequestDto;
import com.colour.board.api.post.domain.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long postId);

    List<Post> findByTitle(String title);

    void update(Long postId, PostRequestDto dto);

    void delete(Long postId, PostRequestDto dto);

    void likePost(Long postId);

    void dislikePost(Long postId);

    void increaseViews(Long postId);
}
