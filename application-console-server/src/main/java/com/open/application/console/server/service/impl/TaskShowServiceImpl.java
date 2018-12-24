package com.open.application.console.server.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.open.application.common.models.Task;
import com.open.application.common.service.TaskShowService;
import com.open.application.console.server.dao.TaskShowDao;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
public class TaskShowServiceImpl implements TaskShowService {

  @Autowired
  private TaskShowDao taskShowDao;

  @Override
  public List<Task> showTaskDataByUid(String uid, Integer offSet, Integer limit) {

    return taskShowDao.getTaskByUid(uid,offSet,limit);
  }

  @Override
  public Integer countTaskDataByUid(String uid) {
    return taskShowDao.getCountTaskDataByUid(uid);
  }


}
