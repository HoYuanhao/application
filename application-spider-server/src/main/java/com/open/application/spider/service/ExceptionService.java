package com.open.application.spider.service;

import com.open.application.common.models.ExceptionModel;
import com.open.application.spider.dao.ExceptionDao;
import com.open.application.spider.elasticsearch.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HeYuanHao
 * @date 2019/2/13 下午4:16
 */
@Service
@Slf4j
public class ExceptionService {
@Autowired
private ExceptionDao exceptionDao;
@Autowired
private ElasticSearchService elasticSearchService;

  public void insertExceptionIntoEsAndDB(ExceptionModel exceptionModel){
    try{
      exceptionDao.insertException(exceptionModel);
      elasticSearchService.insertException(exceptionModel);
    }catch (Exception e){
      log.error("insert exception error",e);
    }
  }
}
