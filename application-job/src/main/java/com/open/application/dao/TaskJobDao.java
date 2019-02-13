package com.open.application.dao;

import com.open.application.common.models.Task;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * @author HeYuanHao
 * @date 2019/1/21 下午12:03
 */
public interface TaskJobDao {

  @Select("SELECT tid ,`status`,uid FROM task WHERE is_delete = 0 AND (status=1 or status =2) limit 0,100")
  List<Task> selectRunTask();

  @Update("UPDATE task set status=#{status} where tid=#{tid}")
  void updateTaskStatus(@Param("tid") String tid, @Param("status") String status);
@Update("Update task set end_time=#{endTime}  where tid=#{tid}")
  void updateTaskEndTime(@Param("tid")String tid, @Param("endTime")Date endTime);

}
