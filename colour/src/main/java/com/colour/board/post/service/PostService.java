package com.colour.board.post.service;

import com.colour.board.post.dto.PostDto;
import com.colour.board.post.dto.PostSearchCond;
import com.colour.board.post.entity.Post;

import java.util.List;

public interface PostService {

    Post createPost(Post post);
    void updatePost(Long postId, PostDto dto);
    Post findPostById(Long postId);
    List<Post> findPostByCond(PostSearchCond cond);
    void deletePost(Long boardId);
}
