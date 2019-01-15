package com.open.application.console.controller;

import com.alibaba.fastjson.JSONObject;
import com.open.application.common.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HeYuanHao
 * @date 2019/1/15 下午3:33
 */
@RestController
@RequestMapping("spider")
public class SpiderController {
  private static final Integer STOP = 1;
  private static final Integer START = 2;
  private static final Integer DELETE = 3;
  @Autowired
  private SpiderService spiderServicel;

  @RequestMapping("operation")
  @ResponseBody
  public JSONObject operationSpider(String taskId, String token, Integer operation) {
    JSONObject jsonObject = new JSONObject();
    if (STOP.equals(operation)) {
      spiderServicel.stopSpider(taskId);
      jsonObject.put("status", 1);
    } else if (START.equals(operation)) {
      spiderServicel.runSpider(taskId);
      jsonObject.put("status", 1);
    } else if (DELETE.equals(operation)) {
      spiderServicel.deleteSpider(taskId);
      jsonObject.put("status", 1);
    } else {
      jsonObject.put("status", 0);
    }
    return jsonObject;
  }


}
