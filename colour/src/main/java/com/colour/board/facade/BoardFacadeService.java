package com.colour.board.facade;

import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.hashtag.domain.entity.Hashtag;
import com.colour.board.api.hashtag.service.HashtagService;
import com.colour.board.api.likes.domain.dto.LikesDto;
import com.colour.board.api.likes.domain.entity.Likes;
import com.colour.board.api.likes.service.LikesService;
import com.colour.board.api.post.domain.dto.PostDto;
import com.colour.board.api.post.domain.entity.Post;
import com.colour.board.api.post.domain.enums.PostStatus;
import com.colour.board.api.post.service.PostService;
import com.colour.board.api.posthashtag.domain.entity.PostHashtag;
import com.colour.board.api.posthashtag.service.PostHashtagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BoardFacadeService {

    private final PostService postService;
    private final HashtagService hashtagService;
    private final PostHashtagService postHashtagService;
    private final LikesService likesService;

    public BoardFacadeService(PostService postService, HashtagService hashtagService,
                              PostHashtagService postHashtagService, LikesService likesService) {
        this.postService = postService;
        this.hashtagService = hashtagService;
        this.postHashtagService = postHashtagService;
        this.likesService = likesService;
    }

    //crud_board
    public Post createTempBoard(Long memberId) {
        return postService.createTemporaryPost(new Post(memberId));
    }

    public void createBoard(PostDto dto, Long postId, Long memberId) {
        dto.setStatus(PostStatus.POSTED.getStatus());
        postService.updatePost(postId, dto, memberId);
    }

    public void updateBoard(PostDto dto, Long postId, long memberId) {
        postService.updatePost(postId, dto, memberId);
    }

    public void deleteBoard(Long postId, long memberId) {
        postService.deletePost(postId, memberId);
    }

    //hashtag
    public void createHashtag(HashtagDto dto, long postId, long memberId) {
        Hashtag hashtag = hashtagService.createHashtag(new Hashtag(dto.getHashtag()));
        if (!postHashtagService.isPostHashtagExist(postId, hashtag.getHashtagId())) {
            postHashtagService.create(new PostHashtag(hashtag.getHashtagId(), postId, memberId));
        } else hashtagService.deleteHashtag(hashtag.getHashtagId());
        // TODO: 이미 등록된 태그입니다.
    }

    public void deleteHashtag(long hashtagId, long postId) {
        if (postHashtagService.isPostHashtagExist(postId, hashtagId)) {
            postHashtagService.delete(postId, hashtagId);
            hashtagService.deleteHashtag(hashtagId);
        } else return; // TODO: 이미 삭제된 태그입니다.
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
