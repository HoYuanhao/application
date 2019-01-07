package com.open.application.common.models;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskMessage {
 private String id;
 private String type;
 private String name;
 private String describe;
 private Date createTime;
 private Date endTime;
 private Integer status;
 private Integer processNum;
 private Integer alarm;
 private Date startTime;

}
