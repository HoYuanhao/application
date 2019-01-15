package com.open.application.common.service;

import com.open.application.common.models.Task;
import com.open.application.common.models.TaskMessage;

import java.util.List;
import java.util.Map;

public interface TaskShowService {

  List<Task> showTaskDataByUid(String uid, Integer offSet, Integer limit,boolean showrun);

  Integer countTaskDataByUid(String uid,boolean showRun);

  Map<String, Object> searchTaskByNameOrDescribeAndByUid(String uid, Integer offset, Integer limit,
      String name, String describe, Integer status, String type);

  void insertTaskToElasticSearchAndDB(Task task);

  List<String> getTaskIDs(String uid);

  List<String> getExceptionTypeByTidAndUid(String tid,String uid);

  List<TaskMessage> getTaskMessageByUid(String uid);
}
