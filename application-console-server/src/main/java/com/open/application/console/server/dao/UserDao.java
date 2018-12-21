package com.open.application.console.server.dao;

import com.open.application.common.models.UserInfo;
import org.apache.ibatis.annotations.Param;

public interface UserDao {

  UserInfo getUserInfoByUserNameAndPassword(@Param("userName") String userName,@Param("password") String password);

}
