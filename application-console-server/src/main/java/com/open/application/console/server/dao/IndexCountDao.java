package com.open.application.console.server.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @author HeYuanHao
 * @date 2019/1/15 下午5:28
 */
public interface IndexCountDao {
  @Select("select sum(process_num) from task")
  Integer getProcessNumSum();

  @Select("select count(1) from task ")
  Integer getTaskNum();

  @Select("select count(1) from exception,task where task.tid=exception.tid")
  Integer getExceptionNum();

  @Select("select count(1) from tb_all_music")
  Integer getMusicNum();

  @Select("select count(1) from tb_all_singer")
  Integer getSingerNum();
  @Select("select count(1) from tb_comments")
  Integer getCommentsNum();
  @Select("select count(1) from tb_playlist")
  Integer getPlaylistNum();
  @Select("select count(1) from tb_playlist_music")
  Integer getPlaylistMusicNum();

}
