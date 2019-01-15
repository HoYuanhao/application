package com.open.application.console.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.open.application.common.models.Task;
import com.open.application.common.service.SpiderService;
import com.open.application.console.server.RedisConfig;
import com.open.application.console.server.dao.SpiderDao;
import com.open.application.console.server.dao.TaskShowDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

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


  @Override
  @Transactional(rollbackFor = Exception.class)
  public void stopSpider(String taskId) {
    spiderDao.changeStatusByTid(taskId, 0);
    redisTemplate.delete("status_" + taskId);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void runSpider(String taskId) {
    spiderDao.changeStatusByTid(taskId, 1);
    redisTemplate.delete("status_" + taskId);
      redisTemplate.opsForValue().set("status_" + taskId, JSON.toJSONString(new HashMap() {
        {
          put("status", 1);
        }
      }));

      redisTemplate.expire("status_" + taskId, 30, TimeUnit.DAYS);
      Task task = taskShowDao.getTaskByTid(taskId);
      if(task!=null) {
        for (int i = 0; i < task.getProcessNum(); i++) {
          productor.sendMessage(new Message(topic, JSON.toJSONString(task).getBytes()));
        }
      }

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteSpider(String taskId) {
    redisTemplate.delete("status_" + taskId);
    spiderDao.changeStatusByTid(taskId,0);
    spiderDao.deleteByTid(taskId);
  }
}
