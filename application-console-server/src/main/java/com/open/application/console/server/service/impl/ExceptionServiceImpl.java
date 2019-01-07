package com.open.application.console.server.service.impl;

import com.open.application.common.models.ExceptionModel;
import com.open.application.common.service.ExceptionService;
import com.open.application.console.server.dao.ExceptionDao;
import com.open.application.console.server.es.service.ElasticSearchService;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ExceptionServiceImpl implements ExceptionService {

  @Autowired
  private ElasticSearchService elasticSearchService;
  @Autowired
  private ExceptionDao exceptionDao;

  @Override
  public Map<String, Object> searchExceptions(String uid, String tid, String type, String key,
      Integer offset, Integer limit) {
    Map<String, Object> map = null;
    try {
      map = elasticSearchService.searchException(uid, tid, type, key, offset, limit);
    } catch (IOException e) {
      log.error("search exception error tid:{} uid:{}", tid, uid, e);
    }
    return map;
  }

  @Override
  public void exceptionLog(ExceptionModel exceptionModel) {
    try {
      exceptionDao.insertException(exceptionModel);
      elasticSearchService.insertException(exceptionModel);
    } catch (Exception e) {
      log.error("insert exception error tid:{} uid:{}", exceptionModel.getTid(),
          exceptionModel.getUid(), e);
    }

  }
}
