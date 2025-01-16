package com.colour.board.post.service;

import com.colour.board.post.dto.PostDto;
import com.colour.board.post.entity.Post;

import java.util.List;

public interface PostService {

    Post createPost(Post post);
    void updatePost(Long postId, PostDto dto);
    Post findPostById(Long postId);
    List<Post> findPostByTitle(String title);
    void deletePost(Long boardId);
}
