package com.colour.board.api.posthashtag.repository;

import com.colour.board.api.posthashtag.dto.PostHashtagDto;
import com.colour.board.api.posthashtag.entity.PostHashtag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostHashtagRepository {

    PostHashtag save(PostHashtag postHashtag);

    List<PostHashtag> findByCond(PostHashtagDto tagPostDto);

    void delete(Long tagPostId);

    void delete(Long postId, Long memberId);
}
