package com.open.application.common.service;

import java.util.Map;

/**
 * @author HeYuanHao
 * @date 2019/1/15 下午5:25
 */
public interface IndexCountService {

   Map<String,Integer> getPanelGroupDataCount(String uid);
}
