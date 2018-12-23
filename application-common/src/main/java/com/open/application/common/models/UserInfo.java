package com.open.application.common.models;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserInfo implements Serializable {
  private String uid;
  private String username;
  private String role;
  private Date createTime;
  private Integer isDeleted;
  private Integer isBanned;
  private String token;


}
