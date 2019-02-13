package com.open.application.spider.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.open.application.common.models.ExceptionModel;
import com.open.application.common.models.TbAllSinger;
import com.open.application.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author HeYuanHao
 * @date 2019/1/17 下午5:12
 */
@Service
@Slf4j
public class ElasticSearchService {
  @Autowired
  private RestHighLevelClient client;

  public void insertSingerToEs(TbAllSinger tbAllSinger) {
    try {
      SearchRequest searchRequest = new SearchRequest("singer_*").types("_doc").source(SearchSourceBuilder
                                                                                         .searchSource()
                                                                                         .query(QueryBuilders.termQuery("singerId", tbAllSinger.getSingerId())));
      SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
      if (searchResponse.getHits().totalHits == 0) {
        IndexRequest indexRequest = new IndexRequest("singer_" + DateUtil.getNowDayString());
        indexRequest.type("_doc");
        indexRequest.source(JSON.toJSONString(tbAllSinger), XContentType.JSON);
        client.index(indexRequest, RequestOptions.DEFAULT);
      }
    } catch (IOException e) {
      log.error("insert singer error", e);
    }
  }

  public void insertException(ExceptionModel exceptionModel) throws IOException {
    IndexRequest indexRequest = new IndexRequest("exception_" + DateUtil.getNowDayString()).type("_doc");
    indexRequest.source(JSONObject.toJSONString(exceptionModel), XContentType.JSON);
    client.index(indexRequest, RequestOptions.DEFAULT);
  }

}
