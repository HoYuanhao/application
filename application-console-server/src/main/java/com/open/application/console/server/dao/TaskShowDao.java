package com.open.application.console.server.dao;

import com.open.application.common.models.Task;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

public interface TaskShowDao {

  List<Task> getTaskByUid(@Param("uid") String uid, @Param("offset") Integer offSet,
      @Param("limit") Integer limit);

  Integer getCountTaskDataByUid(@Param("uid")String uid);

  Cursor<String> getTaskIDsByUid(@Param("uid")String uid);
  

}
