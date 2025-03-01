package com.colour.board.facade;

import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.hashtag.domain.entity.Hashtag;
import com.colour.board.api.hashtag.service.HashtagService;
import com.colour.board.api.posthashtag.domain.dto.PostHashtagDto;
import com.colour.board.api.posthashtag.domain.entity.PostHashtag;
import com.colour.board.api.posthashtag.service.PostHashtagService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HashtagFacadeService {

    private final HashtagService hashtagService;
    private final PostHashtagService postHashtagService;

    public HashtagFacadeService(HashtagService hashtagService, PostHashtagService postHashtagService) {
        this.hashtagService = hashtagService;
        this.postHashtagService = postHashtagService;
    }

    //hashtag
    @PreAuthorize("@postOwnerValidator.validatePostOwner(#postId)")
    public void createHashtag(HashtagDto dto, long postId) {
        Hashtag hashtag = hashtagService.createHashtag(new Hashtag(dto.getHashtag()));
        // prevent duplicated request
        if (!postHashtagService.isPostHashtagExist(postId, hashtag.getHashtagId())) {
            postHashtagService.create(new PostHashtag(hashtag.getHashtagId(), postId));
        } else hashtagService.deleteHashtag(hashtag.getHashtagId());
        // TODO: 이미 등록된 태그입니다.
    }

    @PreAuthorize("@postOwnerValidator.validatePostOwner(#dto.postId)")
    public void deleteHashtag(PostHashtagDto dto) {
        if (postHashtagService.isPostHashtagExist(dto.getPostId(), dto.getHashtagId())) {
            postHashtagService.delete(dto.getPostId(), dto.getHashtagId());
            hashtagService.deleteHashtag(dto.getHashtagId());
        } else return; // TODO: 이미 삭제된 태그입니다.
    }
}
