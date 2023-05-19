package com.hss.kgdoctor.controller;

import cn.hutool.json.JSONUtil;
import com.hss.kgdoctor.common.web.Resp;
import com.hss.kgdoctor.domin.KnowledgeDTO;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/search")
public class KnowledgeByESController {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @GetMapping("/es")
    public Resp<KnowledgeDTO> getByKeyword(@RequestParam("key") String key) throws IOException {
        SearchRequest searchRequest = new SearchRequest("knowledge");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchPhraseQueryBuilder query = QueryBuilders.matchPhraseQuery("knowledgeContent", key);
        sourceBuilder.query(query);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        ArrayList<KnowledgeDTO> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            String jsonStr = hit.getSourceAsString();
            KnowledgeDTO bean = JSONUtil.toBean(jsonStr, KnowledgeDTO.class);
            list.add(bean);
        }
        if (list.isEmpty()) {
            return Resp.success("搜索到零条数据，换个关键字试试呢！",list);
        }
        return Resp.success(list);
    }
}
