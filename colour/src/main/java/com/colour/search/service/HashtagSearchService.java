package com.colour.search.service;

import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.search.repository.HashtagSearchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class HashtagSearchService {

    private final HashtagSearchRepository repository;

    public HashtagSearchService(HashtagSearchRepository repository) {
        this.repository = repository;
    }

    public Map<Long, Integer> findPostsByHashtag(List<HashtagDto> hashtags) {
        return repository.hashtagsSearch(hashtags);
    }
}
