package com.colour.search.service;

import com.colour.board.api.colorpalette.domain.dto.ColorCond;
import com.colour.search.repository.ColorSearchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ColorSearchService {

    private final ColorSearchRepository repository;

    public ColorSearchService(ColorSearchRepository repository) {
        this.repository = repository;
    }

    public Map<Long, Integer> findPostsByColor(List<ColorCond> conds) {
        return repository.colorSearch(conds);
    }

}
