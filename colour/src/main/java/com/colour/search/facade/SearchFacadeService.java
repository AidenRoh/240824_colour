package com.colour.search.facade;

import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.colour.board.api.colorpalette.domain.dto.ColorCond;
import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.post.domain.dto.PostResponseDto;
import com.colour.search.domain.enums.WeightType;
import com.colour.search.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import static com.colour.search.domain.enums.WeightType.*;

@Service
@Transactional
public class SearchFacadeService {

    private final ColorSearchService colorSearchService;
    private final HashtagSearchService hashtagSearchService;
    private final ElasticSearchService elasticSearchService;
    private final PostResponseService postResponseService;
    private final WeightCalculationService weightCalculationService;

    public SearchFacadeService(ColorSearchService colorSearchService,
                               HashtagSearchService hashtagSearchService,
                               ElasticSearchService elasticSearchService,
                               PostResponseService postResponseService, WeightCalculationService weightCalculationService) {
        this.colorSearchService = colorSearchService;
        this.hashtagSearchService = hashtagSearchService;
        this.elasticSearchService = elasticSearchService;
        this.postResponseService = postResponseService;
        this.weightCalculationService = weightCalculationService;
    }


    public Page<PostResponseDto> doElasticSearch(String keyword, Locale locale,
                                                 List<ColorCond> colorCondList,
                                                 List<HashtagDto> hashtagList,
                                                 Pageable pageable) throws IOException {
        // union filter and convert them as functionsScore to use then in elastic search
        Map<Long, Double> filterSearchUnion =
                asyncSearchAndUnion(colorCondList, hashtagList, ELASTIC_DEFAULT, ELASTIC_INTERSECTION);
        List<FunctionScore> elasticScoreElement = elasticSearchService.getFunctionScore(filterSearchUnion);
        // convert keyword to elastic query
        Query keywordQuery = elasticSearchService.getQuery(keyword, locale);
        // totalCount will be set in elasticSearchService.doQuery
        AtomicLong totalCount = new AtomicLong(0);
        List<Long> searchFinalList = elasticSearchService.doQuery(keywordQuery, elasticScoreElement, pageable, totalCount);
        return postResponseService.createElasticResponse(searchFinalList, pageable, totalCount.get());
    }

    public Page<PostResponseDto> doFilterSearch(List<ColorCond> colorCondList,
                                                List<HashtagDto> hashtagList,
                                                Pageable pageable) {
        Map<Long, Double> filterSearchUnion =
                asyncSearchAndUnion(colorCondList, hashtagList, FILTER_ONLY_DEFAULT, FILTER_ONLY_INTERSECTION);
        List<Long> searchFinalList = filterSearchUnion.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();
        AtomicLong totalCount = new AtomicLong(searchFinalList.size());
        return postResponseService.createResponse(searchFinalList, pageable, totalCount.get());
    }

    private Map<Long, Double> asyncSearchAndUnion(List<ColorCond> colorFilters, List<HashtagDto> hashtagFilters,
                                                  WeightType defaultType, WeightType intersectionType) {

        CompletableFuture<Map<Long, Integer>> colorFuture = CompletableFuture.supplyAsync(() ->
                colorSearchService.findPostsByColor(colorFilters)
        );
        CompletableFuture<Map<Long, Integer>> hashtagFuture = CompletableFuture.supplyAsync(() ->
                hashtagSearchService.findPostsByHashtag(hashtagFilters)
        );

        try {
            CompletableFuture.allOf(colorFuture, hashtagFuture).join();
            Map<Long, Integer> colorMap = colorFuture.get();
            Map<Long, Integer> hashtagMap = hashtagFuture.get();
            return weightCalculationService.getUnion(colorMap, hashtagMap, defaultType, intersectionType);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("비동기 필터 처리 중 에러");
            return Collections.emptyMap();
        }
    }

}
