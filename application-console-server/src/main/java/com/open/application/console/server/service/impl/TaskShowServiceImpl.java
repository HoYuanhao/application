package com.open.application.console.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.open.application.common.models.Task;
import com.open.application.common.models.TaskMessage;
import com.open.application.common.service.TaskShowService;
import com.open.application.console.server.dao.ExceptionDao;
import com.open.application.console.server.dao.TaskOperateDao;
import com.open.application.console.server.dao.TaskShowDao;
import com.open.application.console.server.es.service.ElasticSearchService;

import java.io.IOException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author HeYuanHao
 */
@Slf4j
public class TaskShowServiceImpl implements TaskShowService {
  private static final String MUSIC_TYPE = "MUSIC";
  private static final String TICKET_TYPE = "TICKET";
  @Autowired
  private ElasticSearchService elasticSearchService;
  @Autowired
  private RedisTemplate<String, String> redisTemplate;
  @Autowired
  private TaskShowDao taskShowDao;
  @Autowired
  private TaskOperateDao taskOperateDao;
  @Autowired
  private ExceptionDao exceptionDao;

  @Override
  public List<Task> showTaskDataByUid(String uid, Integer offSet, Integer limit, boolean showrun) {

    if (showrun) {
      return taskShowDao.getTaskByUid(uid, offSet, limit, Arrays.asList(1, 2));
    }
    return taskShowDao.getTaskByUid(uid, offSet, limit, null);
  }

  @Override
  public Integer countTaskDataByUid(String uid, boolean showRun) {
    if (showRun) {

      return taskShowDao.getCountTaskDataByUid(uid, Arrays.asList(1, 2));
    }
    return taskShowDao.getCountTaskDataByUid(uid, null);
  }

  @Override
  public Map<String, Object> searchTaskByNameOrDescribeAndByUid(String uid,
                                                                Integer offset,
                                                                Integer limit,
                                                                String name,
                                                                String describe,
                                                                Integer status,
                                                                String type) {
    Map<String, Object> map = null;
    try {
      map = elasticSearchService.searchTaskByLikeNameAndDescribe(uid, offset, limit, name, describe, status, type);
      log.info(map.toString());
    } catch (IOException e) {
      log.error("Search Task By Like Name And Describe Error uid={}", uid, e);
    }
    return map;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void insertTaskToElasticSearchAndDB(Task task) {
    try {
      elasticSearchService.insertTask(task);
      taskOperateDao.insertNewTask(task);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Transactional
  @Override
  public List<String> getTaskIDs(String uid) {
    Cursor<String> stringCursor = taskShowDao.getTaskIDsByUid(uid);
    Iterator<String> iterator = stringCursor.iterator();
    List<String> list = new LinkedList<>();
    while (iterator.hasNext()) {
      list.add(iterator.next());
    }
    return list;
  }

  @Transactional
  @Override
  public List<String> getExceptionTypeByTidAndUid(String tid, String uid) {
    Iterator<String> iterator = exceptionDao.getExceptionTypesByTidAndUid(tid, uid).iterator();
    List<String> list = new LinkedList<>();
    while (iterator.hasNext()) {
      list.add(iterator.next());
    }
    return list;
  }

  @Override
  public List<TaskMessage> getTaskMessageByUid(String uid) {
    if (StringUtils.isBlank(uid)) {
      return Lists.newArrayList();
    }
    return taskShowDao.getTaskMessageByUid(uid);
  }

  @Override
  public List<Integer> countDatas(String type) {
    String json = redisTemplate.opsForValue().get("data_near_week_count");
    List<Integer> result = null;
    if (StringUtils.isBlank(json)) {
      result = Lists.newArrayList();
      if (type.equalsIgnoreCase(MUSIC_TYPE)) {
        List<Integer> commentsCount = taskShowDao.countAllCommentsDatasNearWeek();
        List<Integer> musicCount = taskShowDao.countAllMusicDatasNearWeek();
        List<Integer> playListCount = taskShowDao.countAllPlayListDatasNearWeek();
        List<Integer> allPlayListMusicCount = taskShowDao.countAllPlayListMusicDatasNearWeek();
        List<Integer> singerCount = taskShowDao.countAllSingerDatasNearWeek();
        for (int i = 6; i >= 0; i--) {
          result.add(commentsCount.get(i) + musicCount.get(i) + playListCount.get(i) + allPlayListMusicCount.get(i) + singerCount.get(i));
        }
      }
      redisTemplate.opsForValue().set("data_near_week_count", JSON.toJSONString(result));
      redisTemplate.expire("data_near_week_count", 2, TimeUnit.MINUTES);
    } else {
      result = JSON.parseObject(json, List.class);
    }

    return result;
  }

  @Override
  public List<Integer> countExceptions(String type) {
    type=type.toUpperCase();
    String json = redisTemplate.opsForValue().get("exception_near_week_count");
    List<Integer> result = null;
    if (StringUtils.isBlank(json)) {
      result = Lists.newArrayList();
      if (type.equalsIgnoreCase(MUSIC_TYPE)) {
        result = taskShowDao.countMusicExceptionNearWeek(MUSIC_TYPE);
      }
      redisTemplate.opsForValue().set("exception_near_week_count", JSON.toJSONString(result));
      redisTemplate.expire("exception_near_week_count", 2, TimeUnit.MINUTES);
    } else {
      result = JSON.parseObject(json, List.class);
    }
    return result;
  }

  @Override
  public List<Integer> countProcesses(String type) {
    type=type.toUpperCase();
    String json = redisTemplate.opsForValue().get("process_near_week_count");
    List<Integer> result = null;
    if (StringUtils.isBlank(json)) {
      result = Lists.newArrayList();
      if (type.equalsIgnoreCase(MUSIC_TYPE)) {
        result = taskShowDao.countMusicProcessNearWeek(MUSIC_TYPE);
      }
      redisTemplate.opsForValue().set("process_near_week_count", JSON.toJSONString(result));
      redisTemplate.expire("process_near_week_count", 2, TimeUnit.MINUTES);
    } else {
      result = JSON.parseObject(json, List.class);
    }
    if (result != null) {
      for (int i = 0; i < result.size();i++) {
        if(result.get(i)==null){
          result.set(i,0);
        }
      }
    } return result;
  }

  @Override
  public List<Integer> countTasks(String type) {
    type=type.toUpperCase();
    String json = redisTemplate.opsForValue().get("task_near_week_count");
    List<Integer> result = null;
    if (StringUtils.isBlank(json)) {
      result = Lists.newArrayList();
      if (type.equalsIgnoreCase(MUSIC_TYPE)) {
        result = taskShowDao.countTaskNearWeek(MUSIC_TYPE);
      }
      redisTemplate.opsForValue().set("task_near_week_count", JSON.toJSONString(result));
      redisTemplate.expire("task_near_week_count", 2, TimeUnit.MINUTES);
    } else {
      result = JSON.parseObject(json, List.class);
    }
    return result;
  }

  @Override

  public List<Map<String, String>> groupException() {
    String json = redisTemplate.opsForValue().get("group_exception_near_week_count");
    List<Map<String,String>> result = null;
    if (StringUtils.isBlank(json)) {
      result = taskShowDao.groupException();
      redisTemplate.opsForValue().set("group_exception_near_week_count", JSON.toJSONString(result));
      redisTemplate.expire("group_exception_near_week_count", 2, TimeUnit.MINUTES);
    } else {
      result = JSON.parseObject(json, List.class);
    }
    return result;

  }


}
