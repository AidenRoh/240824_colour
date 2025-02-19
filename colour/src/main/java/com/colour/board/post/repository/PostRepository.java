package com.colour.board.post.repository;

import com.colour.board.post.dto.PostDto;
import com.colour.board.post.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long postId);

    List<Post> findByTitle(String title);

    void update(Long postId, PostDto dto);

    void delete(Long postId);

    void likePost(Long postId);

    void dislikePost(Long postId);
}
