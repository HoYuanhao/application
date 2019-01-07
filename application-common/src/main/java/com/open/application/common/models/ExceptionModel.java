package com.open.application.common.models;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExceptionModel {

  private String eid;
  private String type;
  private String detail;
  private Date throwTime;
  private String uid;
  private String pid;
  private String tid;


}
