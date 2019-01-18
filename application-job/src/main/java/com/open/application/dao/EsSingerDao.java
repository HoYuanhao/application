package com.open.application.dao;

import com.open.application.common.models.TbAllMusic;
import com.open.application.common.models.TbAllSinger;
import org.apache.ibatis.cursor.Cursor;

/**
 * @author HeYuanHao
 * @date 2019/1/18 下午4:07
 */
public interface EsSingerDao {
  Cursor<TbAllSinger> getAllSinger();
}
