package com.open.application.console.controller;

import com.alibaba.fastjson.JSONObject;
import com.open.application.common.models.Axis;
import com.open.application.common.models.BarChart;
import com.open.application.common.models.Grid;
import com.open.application.common.models.Legend;
import com.open.application.common.models.PieChart;
import com.open.application.common.models.Series;
import com.open.application.common.models.TaskMessage;
import com.open.application.common.models.Tooltip;
import com.open.application.common.util.DateUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页数据展示控制器
 *
 * @author
 */
@RestController
@Slf4j
@RequestMapping("/data")
public class IndexController {

  @RequestMapping(path = "/showLineChartData", method = RequestMethod.GET)
  public JSONObject showPanelGroupData(String type, String id, String token) {
    JSONObject jsonObject = new JSONObject();
    if (type.equals("datas")) {
      List<Integer> list1 = Arrays.asList(100, 200, 300, 400, 50, 120, 40);
      List<Integer> list2 = Arrays.asList(400, 200, 150, 230, 80, 920, 10);
      jsonObject.put("music", list1);
      jsonObject.put("ticket", list2);
    } else if (type.equals("exceptions")) {
      List<Integer> list1 = Arrays.asList(100, 200, 300, 420, 50, 120, 40);
      List<Integer> list2 = Arrays.asList(200, 200, 100, 232, 80, 200, 50);
      jsonObject.put("music", list1);
      jsonObject.put("ticket", list2);
    } else if (type.equals("processes")) {
      List<Integer> list1 = Arrays.asList(1020, 2010, 3010, 4220, 150, 2120, 240);
      List<Integer> list2 = Arrays.asList(2300, 2200, 1020, 2232, 380, 1200, 350);
      jsonObject.put("music", list1);
      jsonObject.put("ticket", list2);
    } else if (type.equals("tasks")) {
      List<Integer> list1 = Arrays.asList(10220, 201220, 2222, 42220, 15220, 2120, 24220);
      List<Integer> list2 = Arrays.asList(23200, 220220, 1022220, 22232, 380, 1200, 352220);
      jsonObject.put("music", list1);
      jsonObject.put("ticket", list2);
    }
    return jsonObject;
  }


  @RequestMapping(path = "showPanelGroupData")
  public JSONObject showPanelGroupData(String id, String token) {
    JSONObject jsonObject = new JSONObject();
    Map<String, Integer> datas = new HashMap<>();
    datas.put("end", 234212);
    datas.put("duration", 3600);
    jsonObject.put("datas", datas);

    Map<String, Integer> exceptions = new HashMap<>();
    exceptions.put("end", 2212);
    exceptions.put("duration", 2500);
    jsonObject.put("exceptions", exceptions);

    Map<String, Integer> processes = new HashMap<>();
    processes.put("end", 112);
    processes.put("duration", 3100);
    jsonObject.put("processes", processes);

    Map<String, Integer> tasks = new HashMap<>();
    tasks.put("end", 212);
    tasks.put("duration", 2200);
    jsonObject.put("tasks", tasks);

    return jsonObject;

  }

  @RequestMapping(path = "showPieChartData", method = RequestMethod.GET)
  public PieChart showPieChartData(String id, String token) {
    PieChart pieChart = PieChart.builder()
        .tooltip(Tooltip.builder().trigger("item").formatter("{a} <br/>{b} : {c} ({d}%)").build())
        .legend(Legend.builder().left("center").bottom("10").data(Arrays
            .asList("IOException", "NullPointException", "ClassCastException",
                "ArrayIndexOutOfBoundsException", "FileNotFoundException")).build())
        .calculable(true)
        .series(Arrays.asList(
            Series.builder().name("Exception Set Count").type("pie").roseType("radius")
                .radius(Arrays.asList(15, 95)).center(Arrays.asList("50%", "40%"))
                .data(Arrays.asList(new HashMap<String, Object>() {{
                                      put("value", "200");
                                      put("name", "IOException");
                                    }}
                    , new HashMap<String, Object>() {{
                      put("value", "100");
                      put("name", "ClassCastException");
                    }}
                    , new HashMap<String, Object>() {{
                      put("value", "300");
                      put("name", "ArrayIndexOutOfBoundsException");
                    }}
                    , new HashMap<String, Object>() {{
                      put("value", "400");
                      put("name", "FileNotFoundException");
                    }}
                    , new HashMap<String, Object>() {{
                      put("value", "520");
                      put("name", "NullPointException");
                    }}
                )).animationEasing("cubicInOut").animationDuration(3000).build())
        ).build();
    return pieChart;

  }

  @RequestMapping(path = "showBarChartData", method = RequestMethod.GET)
  public BarChart showBarChartData(String id, String token) {
    BarChart barChart = BarChart.builder()
        .tooltip(Tooltip.builder().trigger("axis").axisPointer(new HashMap<String, Object>() {{
          put("type", "shadow");
        }}).build())
        .grid(Grid.builder().top(10).left("2%").right("2%").bottom("3%").containLabel(true).build())
        .xAxis(Arrays.asList(
            Axis.builder().type("category")
                .data(Arrays.asList(DateUtil.getDayBeforeSomeDay(-6) + "日",
                    DateUtil.getDayBeforeSomeDay(-5) + "日", DateUtil.getDayBeforeSomeDay(-4) + "日",
                    DateUtil.getDayBeforeSomeDay(-3) + "日", DateUtil.getDayBeforeSomeDay(-2) + "日",
                    DateUtil.getDayBeforeSomeDay(-1) + "日", DateUtil.getDayBeforeSomeDay(0) + "日"))
                .axisTick(new HashMap<String, Object>() {{
                  put("alignWithLabel", true);
                }}).build()))
        .yAxis(Arrays.asList(Axis.builder().type("value").axisTick(new HashMap<String, Object>() {{
          put("show", false);
        }}).build()))
        .series(Arrays.asList(Series.builder()
            .name("music").type("bar")
            .stack("vistors").barWidth("60%")
            .data(Arrays.asList(10222220, 22222200, 30112210, 1122222, 2232233, 33332233, 12222211))
            .animationDuration(3000)
            .build(), Series.builder()
            .name("ticket").type("bar")
            .stack("vistors").barWidth("60%")
            .data(Arrays.asList(2422244, 122222, 3322233, 12222222, 3422255, 23342225, 222112332))
            .animationDuration(2333)
            .build())).build();
    return barChart;
  }

  @RequestMapping(path="showTransactionTableData",method = RequestMethod.GET)
  private List<TaskMessage> showTransactionTableData(String id,String token){
    List<TaskMessage> list=new ArrayList<>();
    for(int i=0;i<30;i++){
      list.add(TaskMessage.builder()
          .id(UUID.randomUUID().toString().replace("-",""))
          .type(i%3==1?"music":"ticket")
          .describe("Test! It's a task message! and it's so long ")
          .createTime(new Date(System.currentTimeMillis()-1000000000L))
          .endTime(new Date(System.currentTimeMillis()-1000000L))
          .name("task"+i)
          .status(i%2==0?"error":"exception")
          .build());
    }
    return list;
  }

}
