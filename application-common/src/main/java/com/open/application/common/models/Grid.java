package com.open.application.common.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grid {

  private Integer top;
  private String left;
  private String right;
  private String bottom;
  private boolean containLabel;


}
