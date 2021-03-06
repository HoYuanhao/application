package com.open.application.console.server.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

/**
 * @author HeYuanHao
 * @date 2019/1/15 下午4:00
 */
public interface SpiderDao {

  @Update("update task set status=#{status} where tid=#{tid}")
  void changeStatusByTid(@Param("tid") String tid,@Param("status")Integer status);

  @Update("update task set is_delete=1 where tid=#{tid}")
  void deleteByTid(@Param("tid")String tid);

  @Select("select uid from task where tid=#{tid}")
  String getUidByTid(@Param("tid") String tid);

  @Update("Update task set end_time=#{endTime} where tid=#{tid}")
  void updateTaskEndTime(@Param("tid")String tid, @Param("endTime")Date endTime);

  @Update("Update task set  end_time = null where tid=#{tid}")
  void resetTaskEndTime(@Param("tid")String tid);
}
