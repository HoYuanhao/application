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
public class BarChart {
  private Tooltip tooltip;
  private Grid grid;
  private List<Axis> xAxis;
  private List<Axis> yAxis;
  private List<Series> series;

}
