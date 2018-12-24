package com.open.application.common.service;

import com.open.application.common.models.Task;
import java.util.List;

public interface TaskShowService {

  List<Task> showTaskDataByUid(String uid,Integer offSet,Integer limit);
  Integer countTaskDataByUid(String uid);

}
