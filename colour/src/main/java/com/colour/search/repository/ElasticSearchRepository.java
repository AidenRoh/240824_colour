package com.colour.search.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ElasticSearchRepository {

    private final ElasticsearchClient client;

    public ElasticSearchRepository(ElasticsearchClient client) {
        this.client = client;
    }

    public List<Long> searchPosts(Query query, Pageable pageable, AtomicLong totalCount) throws IOException {
        List<SortOptions> sortOptions = new ArrayList<>();
        pageable.getSort().forEach(order -> {
            String fieldName = order.getProperty();
            SortOrder sortOrder = order.isAscending() ? SortOrder.Asc : SortOrder.Desc;
            sortOptions.add(SortOptions.of(s -> // creating SortOptions.Builder
                    s.field(f -> f // f.field indicates it will sort by field
                            .field(fieldName) // what field?
                            .order(sortOrder) // direction
                    )
            ));
        });

        SearchResponse<Map> searchResult = client.search(search -> {
            search.index("post_contents")
                    .query(query)
                    .from(pageable.getPageNumber() * pageable.getPageSize())
                    .size(pageable.getPageSize())
                    .source(src -> src.filter(f -> f.includes("post_id")));
            if (!sortOptions.isEmpty()) search.sort(sortOptions);
            return search;
        }, Map.class);

        totalCount.set(searchResult.hits().total().value());
        return searchResult.hits().hits().stream()
                .map(hit -> ((Number) hit.source().get("post_id")).longValue())
                .toList();
    }

}


