package com.colour.search.service;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.colour.search.domain.enums.SearchField;
import com.colour.search.repository.ElasticSearchRepository;
import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ElasticSearchService {

    private final ElasticSearchRepository repository;


    public ElasticSearchService(ElasticSearchRepository repository) {
        this.repository = repository;
    }

    public List<FunctionScore> getFunctionScore(Map<Long, Double> resultMap) {
        return resultMap.entrySet().stream()
                .map(entry -> FunctionScore.of(fs -> fs
                        .filter(Query.of(q ->
                                q.term(t ->
                                        t.field("post_id").value(entry.getKey())
                                )
                        ))
                        .weight(entry.getValue())))
                .toList();
    }

    public Query getQuery(String keyword, Locale locale) {
        // detect Language
        LanguageDetector languageDetector = new OptimaizeLangDetector().loadModels();
        List<LanguageResult> usedLanguage = languageDetector.detectAll(keyword);
        Supplier<Set<String>> detectedLanguages =
                () -> usedLanguage.stream().map(LanguageResult::getLanguage).collect(Collectors.toSet());
        // generate list of necessary search fields
        List<String> searchFields = SearchField.getSearchField(locale, detectedLanguages);

        return Query.of(qb -> qb
                .multiMatch(m -> m
                        .fields(searchFields)
                        .query(keyword)
                        .type(TextQueryType.BestFields)
                )
        );
    }

    public List<Long> doQuery(Query keywordQuery, List<FunctionScore> functionScores,
                              Pageable pageable, AtomicLong totalCount) throws IOException {
        Query finalQuery = Query.of(q -> q.functionScore(fsq -> fsq
                .query(keywordQuery)  // 검색어 기반 기본 검색
                .functions(functionScores) // post_id별 가중치 적용
                .scoreMode(FunctionScoreMode.Sum)
                .boostMode(FunctionBoostMode.Multiply)
        ));
        return repository.searchPosts(finalQuery, pageable, totalCount);
    }
}
