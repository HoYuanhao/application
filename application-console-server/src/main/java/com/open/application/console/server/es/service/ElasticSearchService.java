package com.open.application.console.server.es.service;

import com.open.application.common.models.ExceptionModel;
import com.open.application.common.models.Task;
import java.io.IOException;
import java.util.Map;

public interface ElasticSearchService {

  Map<String, Object> searchTaskByLikeNameAndDescribe(String uid, Integer offset, Integer limit,
      String name, String describe, Integer status, String type)
      throws IOException;

  void insertTask(Task task) throws IOException;

  Map<String, Object> searchException(String uid,String tid, String type, String key,
      Integer offset, Integer limit) throws IOException;

}
