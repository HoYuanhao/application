package com.open.application.console.server.dao;

import com.open.application.common.models.ExceptionModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

public interface ExceptionDao {

  Cursor<String> getExceptionTypesByTidAndUid(@Param("tid") String tid,@Param("uid") String uid);

  void insertException(ExceptionModel exceptionModel);
}
