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
import com.open.application.common.service.IndexCountService;
import com.open.application.common.service.TaskShowService;
import com.open.application.common.util.DateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页数据展示控制器
 *
 * @author HeYuanHao
 */
@RestController
@Slf4j
@RequestMapping("/data")
public class IndexController {
  @Autowired
  private IndexCountService indexCountService;
  @Autowired
  private TaskShowService taskShowService;

  @RequestMapping(path = "/showLineChartData", method = RequestMethod.GET)
  public JSONObject showPanelGroupData(String type, String id, String token) {
    JSONObject jsonObject = new JSONObject();

    if (type.equals("datas")) {
      List<Integer> list2 = Arrays.asList(0, 0, 0, 0, 0, 0, 0);
      jsonObject.put("music", taskShowService.countDatas("music"));
      jsonObject.put("ticket", list2);
    } else if (type.equals("exceptions")) {
      List<Integer> list2 = Arrays.asList(0, 0, 0, 0, 0, 0, 0);
      jsonObject.put("music", taskShowService.countExceptions("music"));
      jsonObject.put("ticket", list2);
    } else if (type.equals("processes")) {
      List<Integer> list2 = Arrays.asList(0, 0, 0, 0, 0, 0, 0);
      jsonObject.put("music", taskShowService.countProcesses("music"));
      jsonObject.put("ticket", list2);
    } else if (type.equals("tasks")) {
      List<Integer> list2 = Arrays.asList(0, 0, 0, 0, 0, 0, 0);
      jsonObject.put("music", taskShowService.countTasks("music"));
      jsonObject.put("ticket", list2);
    }
    return jsonObject;
  }


  @RequestMapping(path = "showPanelGroupData")
  public JSONObject showPanelGroupData(String id, String token) {
    JSONObject jsonObject = new JSONObject();
    Map<String, Integer> map = indexCountService.getPanelGroupDataCount(id);
    Map<String, Integer> datas = new HashMap<>();
    datas.put("end", map.get("dataNum"));
    datas.put("duration", 3600);
    jsonObject.put("datas", datas);

    Map<String, Integer> exceptions = new HashMap<>();
    exceptions.put("end", map.get("exceptionNum"));
    exceptions.put("duration", 2500);
    jsonObject.put("exceptions", exceptions);

    Map<String, Integer> processes = new HashMap<>();
    processes.put("end", map.get("processNum"));
    processes.put("duration", 3100);
    jsonObject.put("processes", processes);

    Map<String, Integer> tasks = new HashMap<>();
    tasks.put("end", map.get("taskNum"));
    tasks.put("duration", 2200);
    jsonObject.put("tasks", tasks);

    return jsonObject;

  }

  @RequestMapping(path = "showPieChartData", method = RequestMethod.GET)
  public PieChart showPieChartData(String id, String token) {


    PieChart pieChart = PieChart
      .builder()
      .tooltip(Tooltip.builder().trigger("item").formatter("{a} <br/>{b} : {c} ({d}%)").build())
      .legend(Legend
                .builder()
                .left("center")
                .bottom("10")
                .data(Arrays.asList("IOException", "NullPointException", "ClassCastException", "ArrayIndexOutOfBoundsException", "FileNotFoundException"))
                .build())
      .calculable(true)
      .series(Arrays.asList(Series
                              .builder()
                              .name("Exception Set Count")
                              .type("pie")
                              .roseType("radius")
                              .radius(Arrays.asList(15, 95))
                              .center(Arrays.asList("50%", "40%"))
                              .data(new ArrayList<>(taskShowService.groupException()))
                              .animationEasing("cubicInOut")
                              .animationDuration(3000)
                              .build()))
      .build();
    return pieChart;

  }

  @RequestMapping(path = "showBarChartData", method = RequestMethod.GET)
  public BarChart showBarChartData(String id, String token) {

    BarChart barChart = BarChart
      .builder()
      .tooltip(Tooltip.builder().trigger("axis").axisPointer(new HashMap<String, Object>() {{
        put("type", "shadow");
      }}).build())
      .grid(Grid.builder().top(10).left("2%").right("2%").bottom("3%").containLabel(true).build())
      .xAxis(Arrays.asList(Axis
                             .builder()
                             .type("category")
                             .data(Arrays.asList(DateUtil.getDayBeforeSomeDay(-6) + "日", DateUtil.getDayBeforeSomeDay(-5) + "日",
                                                 DateUtil.getDayBeforeSomeDay(-4) + "日", DateUtil.getDayBeforeSomeDay(-3) + "日",
                                                 DateUtil.getDayBeforeSomeDay(-2) + "日", DateUtil.getDayBeforeSomeDay(-1) + "日",
                                                 DateUtil.getDayBeforeSomeDay(0) + "日"))
                             .axisTick(new HashMap<String, Object>() {{
                               put("alignWithLabel", true);
                             }})
                             .build()))
      .yAxis(Arrays.asList(Axis.builder().type("value").axisTick(new HashMap<String, Object>() {{
        put("show", false);
      }}).build()))
      .series(Arrays.asList(Series.builder().name("music").type("bar").stack("vistors").barWidth("60%").data(new ArrayList<>(taskShowService.countDatas("music"))).animationDuration(3000).build(), Series
        .builder()
        .name("ticket")
        .type("bar")
        .stack("vistors")
        .barWidth("60%")
        .data(Arrays.asList(0, 0, 0, 0, 0, 0, 0))
        .animationDuration(2333)
        .build()))
      .build();
    return barChart;
  }

  @RequestMapping(path = "showTransactionTableData", method = RequestMethod.GET)
  private List<TaskMessage> showTransactionTableData(String id, String token) {
    return taskShowService.getTaskMessageByUid(id);
  }

}
