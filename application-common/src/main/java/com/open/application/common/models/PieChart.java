package com.open.application.common.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PieChart {
private Tooltip tooltip;
private Legend legend;
private boolean calculable=true;
private List<Series> series;
}
