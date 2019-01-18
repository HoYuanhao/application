package com.open.application.console.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.open.application.common.service.IndexCountService;
import com.open.application.console.server.dao.IndexCountDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author HeYuanHao
 * @date 2019/1/15 下午5:24
 */

@Slf4j
public class IndexCountServiceImpl implements IndexCountService {
  @Autowired
  private IndexCountDao indexCountDao;
  @Autowired
  private RedisTemplate<String,String> redisTemplate;
  @Override
  public Map<String, Integer> getPanelGroupDataCount(String uid) {
    String json=redisTemplate.opsForValue().get("index_PanelGroupDataCount");
    if(StringUtils.isBlank(json)) {
      Map<String, Integer> map = Maps.newHashMap();
      Integer processNum = indexCountDao.getProcessNumSum();
      if(processNum==null){
        processNum=0;
      }
      Integer taskNum = indexCountDao.getTaskNum();
      Integer exceptionNum = indexCountDao.getExceptionNum();
      Integer dataNum = indexCountDao.getMusicNum() + indexCountDao.getSingerNum() + indexCountDao.getCommentsNum() + indexCountDao.getPlaylistNum() +
        indexCountDao.getPlaylistMusicNum();
      map.put("processNum", processNum);
      map.put("taskNum", taskNum);
      map.put("exceptionNum", exceptionNum);
      map.put("dataNum", dataNum);
      redisTemplate.opsForValue().set("index_PanelGroupDataCount", JSON.toJSONString(map));
      redisTemplate.expire("index_PanelGroupDataCount", 1, TimeUnit.MINUTES);
      return map;
    }else{
      return JSON.parseObject(json,Map.class);
    }

  }
}
