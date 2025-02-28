package com.colour.board.api.hashtag.repository;

import com.colour.board.api.hashtag.domain.entity.Hashtag;
import org.springframework.stereotype.Repository;

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
