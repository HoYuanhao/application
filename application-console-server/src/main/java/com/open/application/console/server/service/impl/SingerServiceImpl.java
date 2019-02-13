package com.open.application.console.server.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.open.application.common.models.TbAllSinger;
import com.open.application.common.service.SingerService;
import com.open.application.console.server.dao.SingerDao;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author HeYuanHao
 * @date 2019/1/17 下午6:55
 */
@Slf4j
public class SingerServiceImpl implements SingerService {
  @Autowired
  private SingerDao singerDao;
  @Autowired
  private RestHighLevelClient client;


  @Override
  public List<TbAllSinger> getSingerLimit(int offSet, int limit) {
    return singerDao.getTbSingerLimit(offSet, limit);
  }

  @Override
  public int countSinger() {
    return singerDao.countSinger();
  }

  @Override
  public Map<String, Object> searchSinger(String singerName, String stringerDesc, String singerId, int offSet, int limit) {
    SearchRequest searchRequest = new SearchRequest("singer_*").types("_doc");
    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource().query(queryBuilder);
    searchRequest.source(searchSourceBuilder);
    if (!StringUtils.isBlank(singerName)) {
      queryBuilder.must(QueryBuilders.matchPhraseQuery("singerName", singerName));
    }
    if (!StringUtils.isBlank(stringerDesc)) {
      queryBuilder.must(QueryBuilders.matchPhraseQuery("singerDesc", stringerDesc));
    }
    if (!StringUtils.isBlank(singerId)) {
      queryBuilder.must(QueryBuilders.matchPhraseQuery("singerId", singerId));
    }
    searchSourceBuilder.from(offSet);
    searchSourceBuilder.size(limit);
    SearchResponse response = null;
    searchSourceBuilder.explain(true);
    try {
      response = client.search(searchRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      log.error("search singer error", e);
    }
    List<TbAllSinger> result = Lists.newArrayList();
    long count = 0;
    if (response != null) {
      SearchHits hits = response.getHits();
      count = hits.totalHits;
      SearchHit[] searchHits = hits.getHits();
      for (SearchHit searchHit : searchHits) {
        Map<String, Object> map = searchHit.getSourceAsMap();
        TbAllSinger tbAllSinger = TbAllSinger
          .builder()
          .singerId(Long.parseLong(map.get("singerId").toString()))
          .getTime(new Date((Long) map.get("getTime")))
          .singerDesc((String) map.get("singerDesc"))
          .singerHref((String) map.get("singerHref"))
          .singerName((String) map.get("singerName"))
          .build();
        result.add(tbAllSinger);

      }
    }
    Map<String, Object> map = Maps.newHashMap();
    map.put("count", count);
    map.put("singers", result);
    return map;
  }


}

