package com.open.application.console.server.service.impl;

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
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author HeYuanHao
 */
@Slf4j
public class TaskShowServiceImpl implements TaskShowService {

  @Autowired
  private ElasticSearchService elasticSearchService;

  @Autowired
  private TaskShowDao taskShowDao;
  @Autowired
  private TaskOperateDao taskOperateDao;
  @Autowired
  private ExceptionDao exceptionDao;

  @Override
  public List<Task> showTaskDataByUid(String uid, Integer offSet, Integer limit,boolean showrun) {

    if(showrun){
    return taskShowDao.getTaskByUid(uid, offSet, limit, Arrays.asList(1,2));
  }
    return taskShowDao.getTaskByUid(uid, offSet, limit,null);
  }

  @Override
  public Integer countTaskDataByUid(String uid,boolean showRun) {
    if(showRun){

      return taskShowDao.getCountTaskDataByUid(uid,Arrays.asList(1,2));
    }
    return taskShowDao.getCountTaskDataByUid(uid,null);
  }

  @Override
  public Map<String, Object> searchTaskByNameOrDescribeAndByUid(String uid, Integer offset,
      Integer limit, String name, String describe, Integer status,String type) {
    Map<String, Object> map = null;
    try {
      map = elasticSearchService
          .searchTaskByLikeNameAndDescribe(uid, offset, limit, name, describe, status,type);
      log.info(map.toString());
    } catch (IOException e) {
      log.error("Search Task By Like Name And Describe Error uid={}", uid, e);
    }
    return map;
  }

  @Override
  public void insertTaskToElasticSearchAndDB(Task task) {
    try {
      taskOperateDao.insertNewTask(task);
      elasticSearchService.insertTask(task);
    } catch (IOException e) {
      log.error("Insert Task Error Uid={}", task.getUid(), e);
    }
  }

  @Transactional
  @Override
  public List<String> getTaskIDs(String uid) {
    Cursor<String> stringCursor=taskShowDao.getTaskIDsByUid(uid);
    Iterator<String> iterator= stringCursor.iterator();
    List<String> list=new LinkedList<>();
    while (iterator.hasNext()){
    list.add(iterator.next());
    }
    return list;
  }

  @Transactional
  @Override
  public List<String> getExceptionTypeByTidAndUid(String tid,String uid) {
    Iterator<String> iterator=exceptionDao.getExceptionTypesByTidAndUid(tid,uid).iterator();
    List<String> list=new LinkedList<>();
    while(iterator.hasNext()){
      list.add(iterator.next());
    }
    return list;
  }

  @Override
  public List<TaskMessage> getTaskMessageByUid(String uid) {
    return taskShowDao.getTaskMessageByUid(uid);
  }


}
