package com.open.application.console.server.dao;


import com.open.application.common.models.TbAllSinger;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author HeYuanHao
 * @date 2019/1/17 下午6:56
 */
public interface SingerDao {
  @Select("SELECT singerId,singerName,singerHref,singerDesc,getTime from tb_all_singer limit #{offSet},#{limit}")
  List<TbAllSinger> getTbSingerLimit(@Param("offSet") int offSet, @Param("limit") int limit);
  @Select("SELECT count(1) from tb_all_singer")
  int countSinger();
}
