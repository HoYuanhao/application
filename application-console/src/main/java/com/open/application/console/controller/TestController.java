package com.open.application.console.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class TestController {

  @Value("${test}")
  private String fromValue;

  @RequestMapping("test.json")
  @ResponseBody
  public String test(){
    JSONObject jsonObject=new JSONObject();
    jsonObject.put("test",fromValue);
    return jsonObject.toJSONString();
  }

}
