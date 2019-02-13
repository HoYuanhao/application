package com.open.application.console.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.open.application.common.models.Task;
import com.open.application.common.service.SpiderService;
import com.open.application.common.util.EsUtil;
import com.open.application.console.server.dao.SpiderDao;
import com.open.application.console.server.dao.TaskShowDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author HeYuanHao
 * @date 2019/1/15 下午3:57
 */
@Slf4j
public class SpiderServiceImpl implements SpiderService {
  @Autowired
  private RedisTemplate<String, String> redisTemplate;
  @Autowired
  private SpiderDao spiderDao;
  @Autowired
  private Productor productor;
  @Autowired
  private TaskShowDao taskShowDao;
  @Value("${rocket.application.topic}")
  private String topic;
  @Autowired
  private RestHighLevelClient client;


  @Override
  @Transactional(rollbackFor = Exception.class)
  public void stopSpider(String taskId) {
    changeStatus(taskId, 0);
    spiderDao.updateTaskEndTime(taskId,new Date(System.currentTimeMillis()-60000));
    redisTemplate.delete("status_" + taskId);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void runSpider(String taskId) {
    changeStatus(taskId, 1);
    redisTemplate.opsForValue().set("status_" + taskId, JSON.toJSONString(new HashMap() {{ put("status", 1); }}));
    redisTemplate.expire("status_" + taskId, 30, TimeUnit.DAYS);
    Task task = taskShowDao.getTaskByTid(taskId);
    if (task != null) {
      spiderDao.resetTaskEndTime(taskId);
      productor.sendMessage(new Message(topic, JSON.toJSONString(task).getBytes()));
    }

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteSpider(String taskId) {
    String uid = spiderDao.getUidByTid(taskId);
    String index = EsUtil.getTaskIndex(uid);
    DeleteByQueryRequest request = new DeleteByQueryRequest(index)
      .types("_doc")
      .setSize(1).setBatchSize(1)
      .setQuery(QueryBuilders.termQuery("tid", taskId));
    try {
      client.deleteByQuery(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      log.error("delete error", e);
    }
    redisTemplate.delete("status_" + taskId);
    spiderDao.changeStatusByTid(taskId, 0);
    spiderDao.deleteByTid(taskId);
    spiderDao.updateTaskEndTime(taskId,new Date(System.currentTimeMillis()-60000));
  }

  private void changeStatus(String taskId, int status) {
    String uid = spiderDao.getUidByTid(taskId);
    String index = EsUtil.getTaskIndex(uid);
    UpdateByQueryRequest updateRequest = new UpdateByQueryRequest(index)
      .setQuery(QueryBuilders.termQuery("tid", taskId))
       .setRefresh(true).setScript(new Script(
      "ctx._source.status = " + status)).setSize(1).setBatchSize(1);
    try {
      client.updateByQuery(updateRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      log.error("updateByQuery task error", e);
    }
    spiderDao.changeStatusByTid(taskId, status);
  }
}
