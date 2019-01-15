package com.open.application.console.server.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author HeYuanHao
 * @date 2019/1/15 下午4:00
 */
public interface SpiderDao {

  @Update("update task set status=#{status} where tid=#{tid}")
  void changeStatusByTid(@Param("tid") String tid,@Param("status")Integer status);

  @Update("update task set is_delete=1 where tid=#{tid}")
  void deleteByTid(@Param("tid")String tid);
}
