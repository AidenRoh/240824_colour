package com.colour.board.api.hashtag.service;

import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.hashtag.domain.entity.Hashtag;
import com.colour.board.api.hashtag.repository.HashtagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository repository;

    public HashtagServiceImpl(HashtagRepository repository) {
        this.repository = repository;
    }

    @Override
    public Hashtag createHashtag(Hashtag hashtag) {
        Hashtag tag = findByTag(hashtag.getHashtag());
        if (tag != null) {
            return repository.tagUp(tag.getHashtagId());
        } else return repository.save(hashtag);
    }

    @Override
    public Hashtag findById(Long hashtagId) {
        return repository.findById(hashtagId).orElse(null);
    }

    @Override
    public Hashtag findByTag(String hashtagName) {
        return repository.findByTag(hashtagName).orElse(null);
    }

    @Override
    public List<Hashtag> findByCond(HashtagDto cond) {
        return List.of();
    }

    @Override
    public void deleteHashtag(Long hashtagId) {
        Hashtag tag = findById(hashtagId);
        if (tag.getTagFrequency() > 1) {
            repository.tagDown(hashtagId);
        } else repository.delete(hashtagId);
    }
}
