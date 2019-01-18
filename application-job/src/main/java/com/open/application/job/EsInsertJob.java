package com.open.application.job;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.open.application.common.models.TbAllSinger;
import com.open.application.common.util.DateUtil;
import com.open.application.dao.EsSingerDao;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.ibatis.cursor.Cursor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;

/**
 * @author HeYuanHao
 * @date 2019/1/18 下午4:03
 */
@Component
@JobHandler("esInsertJob")
public class EsInsertJob extends IJobHandler {
  @Autowired
  private RestHighLevelClient client;
  @Autowired
  private EsSingerDao esSingerDao;

  @Override
  @Transactional
  public ReturnT<String> execute(String s) throws Exception {
    Cursor<TbAllSinger> cursor = esSingerDao.getAllSinger();
    Iterator<TbAllSinger> iterator = cursor.iterator();
    BulkRequest bulkRequest = new BulkRequest();
    int i = 0;
    while (iterator.hasNext()) {
      TbAllSinger tbAllSinger = iterator.next();
      SearchRequest searchRequest = new SearchRequest("singer_*").types("_doc").source(SearchSourceBuilder
                                                                                         .searchSource()
                                                                                         .query(QueryBuilders.termQuery("singerId", tbAllSinger.getSingerId())));
      SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
      if (searchResponse.getHits().totalHits == 0) {
        bulkRequest.add(new IndexRequest("singer_" + DateUtil.getNowDayString()).type("_doc").source(JSON.toJSONString(tbAllSinger), XContentType.JSON));
        i++;
      }
      if (i == 100) {
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
        bulkRequest = new BulkRequest();
        i = 0;
      }
    }
    if (i != 0) {
      client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }
    return ReturnT.SUCCESS;
  }
}
