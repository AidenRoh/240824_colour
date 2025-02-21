package com.colour.board.api.post.domain.entity;

import com.colour.board.api.post.domain.enums.PostStatus;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Post {

    private static final long DEFAULT_VALUE = 0;

    private Long postId;
    private Long memberId;
    private String title;
    private String content;
    private Long views;
    private Long likes;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public Post(long memberId, String title, String content) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.likes = DEFAULT_VALUE;
        this.views = DEFAULT_VALUE;
        this.status = PostStatus.POSTED.getStatus();
        this.createdAt = new Timestamp(new Date().getTime());
    }

    public static Post createTemporary(long memberId) {
        Post temporary = new Post(memberId, null, null);
        temporary.setStatus(PostStatus.TEMPORARY.getStatus());
        return temporary;
    }

    public Post() {
    }
}
