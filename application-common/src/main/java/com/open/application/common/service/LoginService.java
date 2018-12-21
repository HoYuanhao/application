package com.open.application.common.service;

import com.open.application.common.models.UserInfo;

public interface LoginService {

  public UserInfo login(String username,String password);

  public UserInfo getUserInfo(String token);

}
