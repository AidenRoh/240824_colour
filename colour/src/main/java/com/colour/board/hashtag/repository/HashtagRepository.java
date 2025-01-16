package com.colour.board.hashtag.repository;

import com.colour.board.hashtag.dto.HashtagVo;
import com.colour.board.hashtag.entity.Hashtag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashtagRepository {

    Hashtag save(Hashtag hashtag);
    void tagUp(Long tagId);
    void tagDown(Long tagId);
    Optional<Hashtag> findById(Long tagId);
    Optional<Hashtag> findByTag(String tagName);
    void delete(Long tagId);
}
