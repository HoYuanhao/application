package com.open.application.console.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.open.application.common.models.LoginInfo;
import com.open.application.common.models.UserInfo;
import com.open.application.common.service.LoginService;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户登录控制器
 * @author HeYuanHao
 */
@RestController
@RequestMapping("login")
@Slf4j
public class LoginController {
  @Reference
  private LoginService loginService;

  @RequestMapping(value = "login",produces = "application/json;charset=utf-8",method = RequestMethod.POST)
  public JSONObject login(@RequestBody LoginInfo loginInfo){
    JSONObject jsonObject = new JSONObject();
    try {
      UserInfo userInfo = loginService.login(loginInfo.getUsername(), loginInfo.getPassword());
      if(userInfo!=null) {
        jsonObject.put("status", 200);
        jsonObject.put("token",userInfo.getToken());
      }else{
        jsonObject.put("status", 400);
      }
    }catch (Exception e){
     log.error("login error,username={}",loginInfo.getUsername(),e);
     jsonObject.put("status",500);
    }
    return jsonObject;

  }

  @RequestMapping(value="/user/info",produces = "application/json;charset=utf-8",method = RequestMethod.GET)
  public JSONObject userInfo(String token){
    UserInfo userInfo=loginService.getUserInfo(token);
    JSONObject jsonObject=new JSONObject();
    if(userInfo==null){
      jsonObject.put("status",400);
    }else{
      jsonObject.put("status",200);
      jsonObject.put("userInfo",userInfo);
      log.info("get username={} and id ={} info",userInfo.getUsername(),userInfo.getUid());
    }
    return  jsonObject;
  }

}
