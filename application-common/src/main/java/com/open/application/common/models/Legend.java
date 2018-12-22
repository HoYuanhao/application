package com.open.application.common.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Legend {
  private String left="center";
  private String bottom="10";
  private List<String> data;


}
