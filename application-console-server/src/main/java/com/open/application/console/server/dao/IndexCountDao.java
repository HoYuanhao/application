package com.open.application.console.server.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @author HeYuanHao
 * @date 2019/1/15 下午5:28
 */
public interface IndexCountDao {
  @Select("select sum(process_num) from task where uid=#{uid} group by uid ")
  Integer getProcessNumSum(@Param("uid") String uid);

  @Select("select count(1) from task where uid=#{uid}")
  Integer getTaskNum(@Param("uid") String uid);

  @Select("select count(1) from exception where uid=#{uid}")
  Integer getExceptionNum(@Param("uid")String uid);

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
