package com.open.application.common.service;

import com.open.application.common.models.ExceptionModel;
import java.util.List;
import java.util.Map;

public interface ExceptionService {

  Map<String,Object> searchExceptions(String uid,String tid,String type,String key,Integer offset,Integer limit);
}
