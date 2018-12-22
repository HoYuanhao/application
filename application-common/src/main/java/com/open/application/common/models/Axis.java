package com.open.application.common.models;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Axis {
  private String type;
  private List<String> data;
  private Map<String,Object> axisTick;

}
