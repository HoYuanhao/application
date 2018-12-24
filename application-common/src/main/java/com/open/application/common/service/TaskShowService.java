package com.open.application.common.service;

import com.open.application.common.models.Task;
import java.util.List;
import java.util.Map;

public interface TaskShowService {

  List<Task> showTaskDataByUid(String uid,Integer offSet,Integer limit);
  Integer countTaskDataByUid(String uid);
  Map<String,Object> searchTaskByNameOrDescribeAndByUid(String uid,Integer offset,Integer limit,String name,String describe);
  void insertTaskToElasticSearchAndDB(Task task);
}
