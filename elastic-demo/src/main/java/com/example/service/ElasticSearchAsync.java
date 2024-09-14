package com.example.service;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
@RequiredArgsConstructor
public class ElasticSearchAsync {


    private final RestHighLevelClient client;

    public CompletableFuture<SearchResponse> searchAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SearchRequest searchRequest = new SearchRequest("ecommerce_orders");
                SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
                sourceBuilder.query(QueryBuilders.matchAllQuery());
                searchRequest.source(sourceBuilder);


                return client.search(searchRequest, RequestOptions.DEFAULT);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }
}
