package com.open.application.common.service;

import com.open.application.common.models.TbAllSinger;

import java.util.List;
import java.util.Map;

/**
 * @author HeYuanHao
 * @date 2019/1/17 下午6:53
 */
public interface SingerService {


   List<TbAllSinger> getSingerLimit(int offSet,int limit);

   int countSinger();

  Map<String,Object> searchSinger(String singerName, String stringerDesc, String singerId, int offSet, int limit);

}
