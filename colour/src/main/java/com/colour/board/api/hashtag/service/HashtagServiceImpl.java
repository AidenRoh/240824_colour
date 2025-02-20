package com.colour.board.api.hashtag.service;

import com.colour.board.api.hashtag.dto.HashtagVo;
import com.colour.board.api.hashtag.entity.Hashtag;
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
    public Hashtag save(Hashtag hashtag) {
        Hashtag tag = findByTag(hashtag.getHashtag());
        if (tag != null) {
            repository.tagUp(tag.getHashtagId());
            tag.setTagFrequency(tag.getTagFrequency() + 1);
            return tag;
        } else return repository.save(hashtag);
    }

    @Override
    public Hashtag findById(Long tagId) {
        return repository.findById(tagId).orElse(null);
    }

    @Override
    public Hashtag findByTag(String tagName) {
        return repository.findByTag(tagName).orElse(null);
    }

    @Override
    public List<Hashtag> findByCond(HashtagVo cond) {
        return List.of();
    }

    @Override
    public void delete(Long tagId) {
        Hashtag tag = findById(tagId);
        if (tag.getTagFrequency() > 1) {
            repository.tagDown(tagId);
        } else repository.delete(tagId);
    }
}
