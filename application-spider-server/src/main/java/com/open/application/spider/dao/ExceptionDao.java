package com.open.application.spider.dao;

import com.open.application.common.models.ExceptionModel;
import org.apache.ibatis.annotations.Insert;

/**
 * @author HeYuanHao
 * @date 2019/2/13 下午4:13
 */
public interface ExceptionDao {

  @Insert("insert into exception(eid,type,detail,throw_time,uid,tid)" +
            "values(#{eid},#{type}),#{detail},#{throw_time},#{uid},#{tid}")
  void insertException(ExceptionModel exceptionModel);
}
