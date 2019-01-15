package com.open.application.console.server.dao;

import com.open.application.common.models.Task;

import java.util.Date;
import java.util.List;

import com.open.application.common.models.TaskMessage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;

public interface TaskShowDao {

  List<Task> getTaskByUid(@Param("uid") String uid, @Param("offset") Integer offSet, @Param("limit") Integer limit, @Param("status") List<Integer> status);

  Integer getCountTaskDataByUid(@Param("uid") String uid, @Param("status") List<Integer> status);

  Cursor<String> getTaskIDsByUid(@Param("uid") String uid);


  Task getTaskByTid(@Param("tid") String tid);






  @Select("select tid as id, type, name, `describe`, create_time as createTime, end_time as endTime, " +
            "status,process_num as processNum,alarm,start_time as startTime" +
            " from task where uid=#{uid}")
  List<TaskMessage> getTaskMessageByUid(@Param("uid") String uid);


}
