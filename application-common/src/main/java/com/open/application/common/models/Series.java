package com.open.application.common.models;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Series {
private String name;
private String type;
private String roseType;
private List<Integer> radius;
private List<String> center;
private List<Object> data;
private String animationEasing;
private Integer animationDuration;
private String stack;
private String barWidth;
}
