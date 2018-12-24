package com.open.application.console.server.es.service;

import com.open.application.common.models.Task;
import java.util.List;

public interface ElasticSearchService {

   List<Task> searchTaskByLikeNameAndDescribe(String index,String uid,Integer offset,Integer limit,String name,String describe);

}
