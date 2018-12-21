package com.open.application.console.server.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.open.application.common.models.UserInfo;
import com.open.application.common.service.LoginService;
import com.open.application.console.server.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = LoginService.class)
public class LoginServiceImpl implements LoginService {
@Autowired
private UserDao userDao;
  @Override
  public UserInfo login(String username, String password) {
    return userDao.getUserInfoByUserNameAndPassword(username,password);
  }
}
