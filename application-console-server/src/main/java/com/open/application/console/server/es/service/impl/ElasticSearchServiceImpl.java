package com.open.application.console.server.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.open.application.common.models.Task;
import com.open.application.common.util.EsUtil;
import com.open.application.console.server.es.service.ElasticSearchService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author HeYuanHao
 */
@Slf4j
@Component
public class ElasticSearchServiceImpl implements ElasticSearchService {

  @Value("${elasticsearch.address}")
  private String address;
  @Value("${elasticsearch.scheme}")
  private String scheme;

  private RestHighLevelClient client;

  @PostConstruct
  public void init() {
    client = new RestHighLevelClient(RestClient.builder(EsUtil.getHttpHost(address, scheme)));
  }

  @Override
  public void insertTask(Task task) throws IOException {
    IndexRequest indexRequest = new IndexRequest(EsUtil.getIndex(task.getTid(), "task", "index"))
        .type(EsUtil.getType());
    indexRequest.source(JSON.toJSONString(task), XContentType.JSON);
    client.index(indexRequest, RequestOptions.DEFAULT);
  }


  @Override
  public Map<String, Object> searchTaskByLikeNameAndDescribe(String uid,
      Integer offset,
      Integer limit, String name, String describe) throws IOException {
    List<Task> taskList = new ArrayList<>();
    Map<String, Object> param = new HashMap<>(2);
    param.put("taskList", taskList);
    SearchRequest searchRequest = new SearchRequest(EsUtil.getIndex(uid, "task", "index"));
    searchRequest.types(EsUtil.getType());
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
        .must(QueryBuilders.termQuery("uid", uid));
    if (name != null && !name.trim().equals("")) {
      boolQueryBuilder.must(QueryBuilders.wildcardQuery("name", name));
    }

    if (describe != null && !describe.trim().equals("")) {
      boolQueryBuilder.must(QueryBuilders.wildcardQuery("describe", describe));
    }
    searchSourceBuilder.from(offset);
    searchSourceBuilder.size(limit);
    searchSourceBuilder.explain(true);
    HighlightBuilder highlightBuilder = new HighlightBuilder().requireFieldMatch(true)
        .preTags("<span style=\"color:red\">").postTags("</span>");

    if (describe != null && !describe.trim().equals("")) {
      highlightBuilder.field("describe");
    }
    if (name != null && !name.trim().equals("")) {
      highlightBuilder.field("name");
    }
    searchSourceBuilder.highlighter(highlightBuilder);
    searchSourceBuilder.query(boolQueryBuilder);
    searchRequest.source(searchSourceBuilder);
    SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits searchHits = response.getHits();
    SearchHit[] hits = searchHits.getHits();
    param.put("totalHits", hits.length);
    for (int i = 0; i < hits.length; i++) {
      SearchHit hit = hits[i];
      Map<String, HighlightField> result = hit.getHighlightFields();
      if (result != null && result.size() > 0) {
        for (Map.Entry entry : result.entrySet()) {
          HighlightField field = (HighlightField) entry.getValue();
          if (field != null) {
            // 取得定义的高亮标签
            Text[] texts = field.fragments();
            // 为title串值增加自定义的高亮标签
            StringBuilder sb = new StringBuilder();
            for (Text t : texts) {
              sb.append(t);
            }
            String h = sb
                .substring(sb.indexOf("<span style=\"color:red\">") + 24, sb.indexOf("</span>"));
            String tmp = hit.getSourceAsMap()
                .get(entry.getKey())
                .toString()
                .replaceAll(h, "<span style =\"color:red\">" + h + "</span>");
            hit.getSourceAsMap().put((String) entry.getKey(), tmp);
          }
        }
      }
      Map<String, Object> sourceAsMap = hit.getSourceAsMap();

      taskList.add(Task.builder()
          .tid((String) sourceAsMap.get("tid"))
          .name((String) sourceAsMap.get("name"))
          .describe((String) sourceAsMap.get("describe"))
          .createTime((Date) sourceAsMap.get("createTime"))
          .startTime((Date) sourceAsMap.get("startTime"))
          .endTime((Date) sourceAsMap.get("endTime"))
          .type((String) sourceAsMap.get("type"))
          .processNum((Integer) sourceAsMap.get("processNum"))
          .uid((String) sourceAsMap.get("uid"))
          .isDelete((Integer) sourceAsMap.get("isDelete"))
          .status((Integer) sourceAsMap.get("status"))
          .alarm((Integer) sourceAsMap.get("alarm"))
          .build());
    }
    return param;
  }


  @PreDestroy
  public void destroy() {
    if (client != null) {
      try {
        client.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
