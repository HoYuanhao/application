package com.open.application.common.models;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Task implements Serializable {
  private String tid;
  private String name;
  private String describe;
  private Date createTime;
  private Date startTime;
  private Date endTime;
  private String type;
  private Integer processNum;
  private String uid;
  private Integer isDelete;
  private Integer status;
  private Integer alarm;
  private String source;
}
