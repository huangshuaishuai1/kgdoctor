package com.hss.kgdoctor.controller;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/create")
@Slf4j
public class CreateIndexController {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    // 创建索引和映射
    @GetMapping("/index")
    public String testIndexAndMapping() throws IOException {

        CreateIndexRequest createIndexRequest = new CreateIndexRequest("products");
        createIndexRequest.mapping(
                "{\"properties\": {\n" +
                        "      \"pro_name\":{\n" +
                        "        \"type\": \"text\"\n" +
                        "      },\n" +
                        "      \"price\":{\n" +
                        "        \"type\": \"double\"\n" +
                        "      },\n" +
                        "      \"description\":{\n" +
                        "        \"type\": \"text\"\n" +
                        "      }\n" +
                        "    }}", XContentType.JSON
        );
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices()
                .create(createIndexRequest, RequestOptions.DEFAULT);
        restHighLevelClient.close();
        return String.valueOf(createIndexResponse.isAcknowledged());

    }

    /**
     * 插入文档
     * @return
     * @throws IOException
     */
    @RequestMapping("/documentOne")
    public String testAddOneDocument() throws IOException {
        IndexRequest indexRequest = new IndexRequest("products");
        indexRequest.id("1").source(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"pro_name\":\"macbook pro m1 pro\",\n" +
                        "  \"price\":9729.0,\n" +
                        "  \"description\":\"强悍的 m1 pro芯片\"\n" +
                        "}",XContentType.JSON
        );
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        restHighLevelClient.close();
        return String.valueOf(indexResponse.status());
    }

    /**
     * 更新文档
     * @return
     * @throws IOException
     */
    @RequestMapping("/documentUpdate")
    public String testUpdateOneDocument() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("products","1");
        updateRequest.doc(
                "{\n" +
                        "  \"price\":9724.0\n" +
                        "}",XContentType.JSON
        );
        UpdateResponse UpdateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        restHighLevelClient.close();
        return String.valueOf(UpdateResponse.status());
    }

    /**
     * 基于Id查询文档
     */
    @RequestMapping("/documentGetByID")
    public String testgetDocutmentById() throws IOException {
        GetRequest getRequest = new GetRequest("products","1");
        GetResponse documentFields = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        restHighLevelClient.close();
        return documentFields.getSourceAsString();
    }

    /**
     * 查询：search
     */
    @RequestMapping("/serchData")

    public String serchData() throws IOException {
        SearchRequest searchRequest = new SearchRequest("products");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery()); // 查询
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        restHighLevelClient.close();
        log.info(String.valueOf(searchResponse.getHits().getTotalHits().value));
        return searchResponse.getHits().getHits().toString();
    }


}
