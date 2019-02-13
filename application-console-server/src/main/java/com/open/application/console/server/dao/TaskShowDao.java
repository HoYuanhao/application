package com.open.application.console.server.dao;

import com.open.application.common.models.Task;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.open.application.common.models.TaskMessage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;

public interface TaskShowDao {

  List<Task> getTaskByUid(@Param("uid") String uid, @Param("offset") Integer offSet, @Param("limit") Integer limit, @Param("status") List<Integer> status);

  Integer getCountTaskDataByUid(@Param("uid") String uid, @Param("status") List<Integer> status);

  Cursor<String> getTaskIDsByUid(@Param("uid") String uid);


  Task getTaskByTid(@Param("tid") String tid);


  @Select("select count(1) from task where uid=#{uid}")
  int getCountTaskByUid(@Param("uid") String uid);

  @Select("select tid as id, type, name, `describe`, create_time as createTime, end_time as endTime, " +
            "status,process_num as processNum,alarm,start_time as startTime  from task where uid=#{uid} order by create_time desc limit 0,100")
  List<TaskMessage> getTaskMessageByUid(@Param("uid") String uid);

  @Select("select count(1) from  tb_all_music where CURDATE()<= getTime and getTime <=DATE_ADD(CURDATE(),INTERVAL 1 day)\n" + "UNION ALL\n" +
            "select count(1) from  tb_all_music where DATE_SUB(CURDATE(),INTERVAL 1 day)<= getTime and getTime <=CURDATE()\n" + "UNION ALL\n" +
            "select count(1) from  tb_all_music where DATE_SUB(CURDATE(),INTERVAL 2 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 1 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_all_music where DATE_SUB(CURDATE(),INTERVAL 3 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 2 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_all_music where DATE_SUB(CURDATE(),INTERVAL 4 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 3 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_all_music where DATE_SUB(CURDATE(),INTERVAL 5 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 4 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_all_music where DATE_SUB(CURDATE(),INTERVAL 6 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 5 day)")
  List<Integer> countAllMusicDatasNearWeek();

  @Select("select count(1) from  tb_all_singer where CURDATE()<= getTime and getTime <=DATE_ADD(CURDATE(),INTERVAL 1 day)\n" + "UNION ALL\n" +
            "select count(1) from  tb_all_singer where DATE_SUB(CURDATE(),INTERVAL 1 day)<= getTime and getTime <=CURDATE()\n" + "UNION ALL\n" +
            "select count(1) from  tb_all_singer where DATE_SUB(CURDATE(),INTERVAL 2 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 1 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_all_singer where DATE_SUB(CURDATE(),INTERVAL 3 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 2 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_all_singer where DATE_SUB(CURDATE(),INTERVAL 4 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 3 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_all_singer where DATE_SUB(CURDATE(),INTERVAL 5 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 4 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_all_singer where DATE_SUB(CURDATE(),INTERVAL 6 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 5 day)")
  List<Integer> countAllSingerDatasNearWeek();

  @Select("select count(1) from  tb_comments where CURDATE()<= getTime and getTime <=DATE_ADD(CURDATE(),INTERVAL 1 day)\n" + "UNION ALL\n" +
            "select count(1) from  tb_comments where DATE_SUB(CURDATE(),INTERVAL 1 day)<= getTime and getTime <=CURDATE()\n" + "UNION ALL\n" +
            "select count(1) from  tb_comments where DATE_SUB(CURDATE(),INTERVAL 2 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 1 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_comments where DATE_SUB(CURDATE(),INTERVAL 3 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 2 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_comments where DATE_SUB(CURDATE(),INTERVAL 4 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 3 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_comments where DATE_SUB(CURDATE(),INTERVAL 5 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 4 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_comments where DATE_SUB(CURDATE(),INTERVAL 6 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 5 day)")
  List<Integer> countAllCommentsDatasNearWeek();

  @Select("select count(1) from  tb_playlist where CURDATE()<= getTime and getTime <=DATE_ADD(CURDATE(),INTERVAL 1 day)\n" + "UNION ALL\n" +
            "select count(1) from  tb_playlist where DATE_SUB(CURDATE(),INTERVAL 1 day)<= getTime and getTime <=CURDATE()\n" + "UNION ALL\n" +
            "select count(1) from  tb_playlist where DATE_SUB(CURDATE(),INTERVAL 2 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 1 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_playlist where DATE_SUB(CURDATE(),INTERVAL 3 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 2 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_playlist where DATE_SUB(CURDATE(),INTERVAL 4 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 3 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_playlist where DATE_SUB(CURDATE(),INTERVAL 5 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 4 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_playlist where DATE_SUB(CURDATE(),INTERVAL 6 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 5 day)")
  List<Integer> countAllPlayListDatasNearWeek();

  @Select("select count(1) from  tb_playlist_music where CURDATE()<= getTime and getTime <=DATE_ADD(CURDATE(),INTERVAL 1 day)\n" + "UNION ALL\n" +
            "select count(1) from  tb_playlist_music where DATE_SUB(CURDATE(),INTERVAL 1 day)<= getTime and getTime <=CURDATE()\n" + "UNION ALL\n" +
            "select count(1) from  tb_playlist_music where DATE_SUB(CURDATE(),INTERVAL 2 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 1 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_playlist_music where DATE_SUB(CURDATE(),INTERVAL 3 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 2 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_playlist_music where DATE_SUB(CURDATE(),INTERVAL 4 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 3 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_playlist_music where DATE_SUB(CURDATE(),INTERVAL 5 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 4 day)\n" +
            "UNION ALL\n" +
            "select count(1) from  tb_playlist_music where DATE_SUB(CURDATE(),INTERVAL 6 day)<= getTime and getTime <=DATE_SUB(CURDATE(),INTERVAL 5 day)")
  List<Integer> countAllPlayListMusicDatasNearWeek();

  @Select("select count(1) from  exception,task where DATE_SUB(CURDATE(),INTERVAL 6 day)<= throw_time and throw_time <=DATE_SUB(CURDATE(),INTERVAL 5 day)\n" +
            "and task.tid=exception.tid and task.type=#{type}\n" + "UNION ALL\n" +
            "select count(1) from  exception,task where DATE_SUB(CURDATE(),INTERVAL 5 day)<= throw_time and throw_time <=DATE_SUB(CURDATE(),INTERVAL 4 day)\n" +
            "and task.tid=exception.tid and task.type=#{type}\n" + "UNION ALL\n" +
            "select count(1) from  exception,task where DATE_SUB(CURDATE(),INTERVAL 4 day)<= throw_time and throw_time <=DATE_SUB(CURDATE(),INTERVAL 3 day)\n" +
            "and task.tid=exception.tid and task.type=#{type}\n" + "UNION ALL\n" +
            "select count(1) from  exception,task where DATE_SUB(CURDATE(),INTERVAL 3 day)<= throw_time and throw_time <=DATE_SUB(CURDATE(),INTERVAL 2 day)\n" +
            "and task.tid=exception.tid and task.type=#{type}\n" + "UNION ALL\n" +
            "select count(1) from  exception,task where DATE_SUB(CURDATE(),INTERVAL 2 day)<= throw_time and throw_time <=DATE_SUB(CURDATE(),INTERVAL 1 day)\n" +
            "and task.tid=exception.tid and task.type=#{type}\n" + "UNION ALL\n" +
            "select count(1) from  exception,task where DATE_SUB(CURDATE(),INTERVAL 1 day)<= throw_time and throw_time <=CURDATE()\n" +
            "and task.tid=exception.tid and task.type=#{type}\n" + "UNION ALL\n" +
            "select count(1) from  exception,task where CURDATE()<= throw_time and throw_time <=DATE_ADD(CURDATE(),INTERVAL 1 day)\n" +
            "and task.tid=exception.tid and task.type=#{type}")
  List<Integer> countMusicExceptionNearWeek(@Param("type") String type);

  @Select("select sum(process_num) from  task where DATE_SUB(CURDATE(),INTERVAL 6 day)<= create_time and create_time <=DATE_SUB(CURDATE(),INTERVAL 5 day)\n" +
            "and type=#{type}\n" + "UNION ALL\n" +
            "select sum(process_num) from  task where DATE_SUB(CURDATE(),INTERVAL 5 day)<= create_time and create_time <=DATE_SUB(CURDATE(),INTERVAL 4 day)\n" +
            "and type=#{type}\n" + "UNION ALL\n" +
            "select sum(process_num) from  task where DATE_SUB(CURDATE(),INTERVAL 4 day)<= create_time and create_time <=DATE_SUB(CURDATE(),INTERVAL 3 day)\n" +
            "and type=#{type}\n" + "UNION ALL\n" +
            "select sum(process_num) from  task where DATE_SUB(CURDATE(),INTERVAL 3 day)<= create_time and create_time <=DATE_SUB(CURDATE(),INTERVAL 2 day)\n" +
            "and type=#{type}\n" + "UNION ALL\n" +
            "select sum(process_num) from  task where DATE_SUB(CURDATE(),INTERVAL 2 day)<= create_time and create_time <=DATE_SUB(CURDATE(),INTERVAL 1 day)\n" +
            "and type=#{type}\n" + "UNION ALL\n" +
            "select sum(process_num) from  task where DATE_SUB(CURDATE(),INTERVAL 1 day)<= create_time and create_time <=CURDATE()\n" + "and type=#{type}\n" +
            "UNION ALL\n" + "select sum(process_num) from  task where CURDATE()<= create_time and create_time <=DATE_ADD(CURDATE(),INTERVAL 1 day)\n" +
            "and type=#{type}")
  List<Integer> countMusicProcessNearWeek(@Param("type") String type);

  @Select("select count(1) from  task where DATE_SUB(CURDATE(),INTERVAL 6 day)<= create_time and create_time <=DATE_SUB(CURDATE(),INTERVAL 5 day)\n" +
            "and type=#{type}\n" + "UNION ALL\n" +
            "select count(1)  from  task where DATE_SUB(CURDATE(),INTERVAL 5 day)<= create_time and create_time <=DATE_SUB(CURDATE(),INTERVAL 4 day)\n" +
            "and type=#{type}\n" + "UNION ALL\n" +
            "select count(1)  from  task where DATE_SUB(CURDATE(),INTERVAL 4 day)<= create_time and create_time <=DATE_SUB(CURDATE(),INTERVAL 3 day)\n" +
            "and type=#{type}\n" + "UNION ALL\n" +
            "select count(1)  from  task where DATE_SUB(CURDATE(),INTERVAL 3 day)<= create_time and create_time <=DATE_SUB(CURDATE(),INTERVAL 2 day)\n" +
            "and type=#{type}\n" + "UNION ALL\n" +
            "select count(1)  from  task where DATE_SUB(CURDATE(),INTERVAL 2 day)<= create_time and create_time <=DATE_SUB(CURDATE(),INTERVAL 1 day)\n" +
            "and type=#{type}\n" + "UNION ALL\n" +
            "select count(1)  from  task where DATE_SUB(CURDATE(),INTERVAL 1 day)<= create_time and create_time <=CURDATE()\n" + "and type=#{type}\n" +
            "UNION ALL\n" + "select count(1)  from  task where CURDATE()<= create_time and create_time <=DATE_ADD(CURDATE(),INTERVAL 1 day)\n" +
            "and type=#{type}")
  List<Integer> countTaskNearWeek(@Param("type") String type);

  @Select("select count(1) as value,type as name from exception where DATE_SUB(CURDATE(),INTERVAL 6 day)<= throw_time and throw_time <= DATE_ADD(CURDATE(),INTERVAL 1 day) group by type order by value")
  List<Map<String, String>> groupException();

}
