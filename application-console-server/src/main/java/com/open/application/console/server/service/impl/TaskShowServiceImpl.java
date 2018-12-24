package com.open.application.console.server.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.open.application.common.models.Task;
import com.open.application.common.service.TaskShowService;
import com.open.application.console.server.dao.TaskOperateDao;
import com.open.application.console.server.dao.TaskShowDao;
import com.open.application.console.server.es.service.ElasticSearchService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

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

  @Override
  public List<Task> showTaskDataByUid(String uid, Integer offSet, Integer limit) {

    return taskShowDao.getTaskByUid(uid,offSet,limit);
  }

  @Override
  public Integer countTaskDataByUid(String uid) {
    return taskShowDao.getCountTaskDataByUid(uid);
  }

  @Override
  public Map<String, Object> searchTaskByNameOrDescribeAndByUid(String uid, Integer offset,
      Integer limit, String name, String describe) {
    Map<String, Object> map=null;
    try {
      map=elasticSearchService.searchTaskByLikeNameAndDescribe(uid,offset,limit,name,describe);
      log.info(map.toString());
    } catch (IOException e) {
     log.error("Search Task By Like Name And Describe Error uid={}",uid,e);
    }
    return map;
  }

  @Override
  public void insertTaskToElasticSearchAndDB(Task task) {
    try {
      elasticSearchService.insertTask(task);
      taskOperateDao.insertNewTask(task);
    } catch (IOException e) {
      log.error("Insert Task Error Uid={}",task.getUid(),e);
    }
  }


}
