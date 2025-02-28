package com.colour.board.facade;

import com.colour.board.api.likes.domain.dto.LikesDto;
import com.colour.board.api.likes.domain.entity.Likes;
import com.colour.board.api.likes.service.LikesService;
import com.colour.board.api.post.domain.dto.PostDto;
import com.colour.board.api.post.domain.entity.Post;
import com.colour.board.api.post.domain.enums.PostStatus;
import com.colour.board.api.post.service.PostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BoardFacadeService {

    private final PostService postService;
    private final LikesService likesService;

    public BoardFacadeService(PostService postService, LikesService likesService) {
        this.postService = postService;
        this.likesService = likesService;
    }

    //crud_board
    public Post createTempBoard(Long memberId) {
        return postService.createTemporaryPost(new Post(memberId));
    }

    @PreAuthorize("@postOwnerValidator.validatePostOwner(#postId)")
    public void createBoard(PostDto dto, Long postId) {
        dto.setStatus(PostStatus.POSTED.getStatus());
        postService.updatePost(postId, dto);
    }

    @PreAuthorize("@postOwnerValidator.validatePostOwner(#postId)")
    public void updateBoard(PostDto dto, Long postId) {
        postService.updatePost(postId, dto);
    }

    @PreAuthorize("@postOwnerValidator.validatePostOwner(#postId)")
    public void deleteBoard(Long postId) {
        postService.deletePost(postId);
    }

    //likes
    public void likePost(LikesDto dto) {
        if (!likesService.isLikeExist(dto)) {
            likesService.likePost(new Likes(dto.getPostId(), dto.getMemberId()));
        } // TODO: 이미 좋아요를 눌렀습니다.
    }

    public void dislikePost(LikesDto dto) {
        if (likesService.isLikeExist(dto)) {
            likesService.dislikePost(dto);
        } else return; // TODO: 이미 취소된 좋아요입니다.
    }

}
