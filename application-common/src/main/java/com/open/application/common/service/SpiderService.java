package com.open.application.common.service;

/**
 * @author HeYuanHao
 * @date 2019/1/15 下午3:55
 */
public interface SpiderService {
   void stopSpider(String taskId);
   void runSpider(String taskId);
   void deleteSpider(String taskId);
}
