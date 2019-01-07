package com.open.application.console.server.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.open.application.common.models.UserInfo;
import com.open.application.common.service.LoginService;
import com.open.application.console.server.dao.UserDao;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

@Slf4j
public class LoginServiceImpl implements LoginService {
@Autowired
private UserDao userDao;
@Autowired
private RedisTemplate<String,String> redisTemplate;
  @Override
  public UserInfo login(String username, String password) {
    UserInfo userInfo=userDao.getUserInfoByUserNameAndPassword(username,password);
    if(userInfo!=null) {
      long time = System.currentTimeMillis();
      String token = DigestUtils.md5DigestAsHex((userInfo.getUid() + time).getBytes());
      redisTemplate.opsForValue().set(token, JSON.toJSONString(userInfo));
      redisTemplate.expire(token, 30, TimeUnit.MINUTES);
      userInfo.setToken(token);
    }
    return userInfo;
  }

  @Override
  public UserInfo getUserInfo(String token) {
    String value=redisTemplate.opsForValue().get(token);
    redisTemplate.expire(token,30, TimeUnit.MINUTES);
    if(value!=null&&!value.trim().equals("")){
      return JSON.toJavaObject(JSONObject.parseObject(value),UserInfo.class);
    }else{
      return null;
    }

  }
}
