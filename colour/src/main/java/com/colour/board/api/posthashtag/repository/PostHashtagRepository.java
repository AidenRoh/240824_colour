package com.colour.board.api.posthashtag.repository;

import com.colour.board.api.posthashtag.domain.dto.PostHashtagDto;
import com.colour.board.api.posthashtag.domain.entity.PostHashtag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostHashtagRepository {

    void save(PostHashtag postHashtag);

    List<PostHashtag> findByCond(PostHashtagDto tagPostDto);

    void delete(long postId, long hashtagId);

    boolean existsByKeys(long postId, long hashtagId);

}
