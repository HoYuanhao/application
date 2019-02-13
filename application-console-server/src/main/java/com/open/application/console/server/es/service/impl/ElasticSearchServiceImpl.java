package com.open.application.console.server.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.io.Files;
import com.open.application.common.models.ExceptionModel;
import com.open.application.common.models.Task;
import com.open.application.common.util.EsUtil;
import com.open.application.console.server.dao.TaskShowDao;
import com.open.application.console.server.es.service.ElasticSearchService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
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
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author HeYuanHao
 */
@Slf4j
@Component
public class ElasticSearchServiceImpl implements ElasticSearchService {
  @Autowired
  private TaskShowDao taskShowDao;

  @Autowired
  private RestHighLevelClient client;



  @Override
  public void insertTask(Task task) throws IOException {
    int count = taskShowDao.getCountTaskByUid(task.getUid());
    if (count == 0) {
      CreateIndexRequest request = new CreateIndexRequest(EsUtil.getTaskIndex(task.getUid())).source(Files.toString(new File(
        this.getClass().getClassLoader().getResource("").getPath() + "/index.json"), Charset.defaultCharset()), XContentType.JSON);
      client.indices().create(request, RequestOptions.DEFAULT);
    }
    IndexRequest indexRequest = new IndexRequest(EsUtil.getTaskIndex(task.getUid())).type(EsUtil.getType());
    String json=JSON.toJSONString(task);
    indexRequest.source(json, XContentType.JSON);
    client.index(indexRequest, RequestOptions.DEFAULT);
  }

  @Override
  public Map<String, Object> searchException(String uid, String tid, String type, String key, Integer offset, Integer limit) throws IOException {
    List<ExceptionModel> list = new ArrayList<>();
    Map<String, Object> result = new HashMap<>();
    result.put("exceptions", list);
    SearchRequest searchRequest = new SearchRequest("exception_*");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("uid", uid)).must(QueryBuilders.termQuery("tid", tid));
    if (type != null && !type.trim().equals("")) {
      boolQueryBuilder.must(QueryBuilders.termQuery("type", type));
    }
    if (key != null && !key.trim().equals("")) {
      boolQueryBuilder.must(QueryBuilders.matchPhrasePrefixQuery("detail", key));
    }
    searchSourceBuilder.size(limit);
    searchSourceBuilder.from(offset);
    // 设置是否按查询匹配度排序
    searchSourceBuilder.explain(true);
    HighlightBuilder highlightBuilder = new HighlightBuilder();
    highlightBuilder.field("detail");
    highlightBuilder.field("type");
    highlightBuilder.requireFieldMatch(true);
    highlightBuilder.preTags("<span style=\"color:red\">");
    highlightBuilder.postTags("</span>");
    searchSourceBuilder.highlighter(highlightBuilder);
    searchRequest.source(searchSourceBuilder);
    SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits searchHits = response.getHits();
    SearchHit[] hits = searchHits.getHits();
    result.put("totalHits", searchHits.totalHits);
    for (int i = 0; i < hits.length; i++) {
      SearchHit hit = hits[i];
      Map<String, HighlightField> highlightFields = hit.getHighlightFields();
      if (highlightFields != null && highlightFields.size() > 0) {
        for (Map.Entry entry : highlightFields.entrySet()) {
          HighlightField field = (HighlightField) entry.getValue();
          if (field != null) {
            // 取得定义的高亮标签
            Text[] texts = field.fragments();
            // 为title串值增加自定义的高亮标签
            StringBuilder sb = new StringBuilder();
            for (Text t : texts) {
              sb.append(t);
            }
            String h = sb.substring(sb.indexOf("<span style=\"color:red\">") + 24, sb.indexOf("</span>"));
            String tmp = hit.getSourceAsMap().get(entry.getKey()).toString().replaceAll(h, "<span style =\"color:red\">" + h + "</span>");
            hit.getSourceAsMap().put((String) entry.getKey(), tmp);
          }
        }
      }
      Map<String, Object> stringObjectMap = hit.getSourceAsMap();
      list.add(ExceptionModel
                 .builder()
                 .eid((String) stringObjectMap.get("eid"))
                 .pid((String) stringObjectMap.get("pid"))
                 .detail((String) stringObjectMap.get("detail"))
                 .throwTime(new Date((Long) stringObjectMap.get("throwTime")))
                 .tid((String) stringObjectMap.get("tid"))
                 .type((String) stringObjectMap.get("type"))
                 .uid((String) stringObjectMap.get("uid"))
                 .build());
    }
    return result;
  }


  @Override
  public Map<String, Object> searchTaskByLikeNameAndDescribe(String uid,
                                                             Integer offset,
                                                             Integer limit,
                                                             String name,
                                                             String describe,
                                                             Integer status,
                                                             String type) throws IOException {
    List<Task> taskList = new ArrayList<>();
    Map<String, Object> param = new HashMap<>(2);
    param.put("taskList", taskList);
    SearchRequest searchRequest = new SearchRequest(EsUtil.getTaskIndex(uid));
    searchRequest.types(EsUtil.getType());
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("uid", uid));
    if (status != null) {
      boolQueryBuilder.must(QueryBuilders.matchQuery("status", status));
    }
    if (type != null && !type.trim().equals("")) {
      boolQueryBuilder.must(QueryBuilders.matchQuery("type", type));
    }
    if (name != null && !name.trim().equals("")) {
      boolQueryBuilder.must(QueryBuilders.matchPhrasePrefixQuery("name", name));
    }
    if (describe != null && !describe.trim().equals("")) {
      boolQueryBuilder.must(QueryBuilders.matchPhrasePrefixQuery("describe", describe));
    }
    searchSourceBuilder.from(offset);
    searchSourceBuilder.size(limit);
    searchSourceBuilder.explain(true);
    searchSourceBuilder.query(boolQueryBuilder);
    searchSourceBuilder.sort("createTime", SortOrder.DESC);
    searchRequest.source(searchSourceBuilder);
    SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits searchHits = response.getHits();
    SearchHit[] hits = searchHits.getHits();
    param.put("totalHits", searchHits.totalHits);
    for (int i = 0; i < hits.length; i++) {
      SearchHit hit = hits[i];
      Map<String, Object> sourceAsMap = hit.getSourceAsMap();
      Task.TaskBuilder taskBuilder = Task
        .builder()
        .tid((String) sourceAsMap.get("tid"))
        .name((String) sourceAsMap.get("name"))
        .describe((String) sourceAsMap.get("describe"))
        .createTime(new Date((Long) sourceAsMap.get("createTime")))
        .type((String) sourceAsMap.get("type"))
        .uid((String) sourceAsMap.get("uid"))
        .processNum((Integer) sourceAsMap.get("processNum"))
        .isDelete((Integer) sourceAsMap.get("isDelete"))
        .status((Integer) sourceAsMap.get("status"))
        .alarm((Integer) sourceAsMap.get("alarm"))
        .source((String) sourceAsMap.get("source"));
      Object startTime = sourceAsMap.get("startTime");
      Object endTime = sourceAsMap.get("endTIme");
      if (endTime != null) {
        taskBuilder.endTime(new Date((Long) endTime));
      }
      if (startTime != null) {
        taskBuilder.startTime(new Date((Long) startTime));
      }
      taskList.add(taskBuilder.build());
    }
    log.info("uid:{} offset:{} limit:{} name:{} describe:{} status:{} type:{}", uid, offset, limit, name, describe, status, type);
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
