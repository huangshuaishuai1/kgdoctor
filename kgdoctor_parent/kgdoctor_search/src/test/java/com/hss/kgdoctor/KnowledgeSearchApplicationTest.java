package com.hss.kgdoctor;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest(classes = KnowledgeSearchApplicationTest.class)
@RunWith(SpringRunner.class)
public class KnowledgeSearchApplicationTest {
    @Autowired
    RestHighLevelClient restHighLevelClient;

    // 创建索引和映射
    @Test
    public void testIndexAndMapping() throws IOException {

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
        System.out.println(createIndexResponse.isAcknowledged());
    }

}
