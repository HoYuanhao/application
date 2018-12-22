package com.open.application.common.models;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tooltip {
private String trigger;
private String formatter;
private Map<String,Object> axisPointer;

}
