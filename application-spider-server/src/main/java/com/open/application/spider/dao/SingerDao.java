package com.open.application.spider.dao;

import com.open.application.common.models.TbAllSinger;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author HeYuanHao
 * @date 2019/1/17 下午3:42
 */
public interface SingerDao {

  @Insert("INSERT ignore INTO\ttb_all_singer(singerId,singerName,singerHref,singerDesc,getTime)" +
            "values(#{singerId},#{singerName},#{singerHref},#{singerDesc},#{getTime})")
  void insertTbAllSinger(TbAllSinger tbAllSinger);

}
