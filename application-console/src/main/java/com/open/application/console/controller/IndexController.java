package com.open.application.console.controller;

import com.alibaba.fastjson.JSONObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页数据展示控制器
 * @author
 */
@RestController
@Slf4j
@RequestMapping("/data")
public class IndexController {

  @RequestMapping(path="/showLineChartData",method = RequestMethod.GET)
  public JSONObject showPanelGroupData(String type,String id,String token){
    JSONObject jsonObject=new JSONObject();
    if(type.equals("datas")){
    List<Integer> list1= Arrays.asList(100,200,300,400,50,120,40);
    List<Integer> list2= Arrays.asList(400,200,150,230,80,920,10);
    jsonObject.put("music",list1);
    jsonObject.put("ticket",list2);
    }else if(type.equals("exceptions")){
      List<Integer> list1= Arrays.asList(100,200,300,420,50,120,40);
      List<Integer> list2= Arrays.asList(200,200,100,232,80,200,50);
      jsonObject.put("music",list1);
      jsonObject.put("ticket",list2);
    }else if(type.equals("processes")){
      List<Integer> list1= Arrays.asList(1020,2010,3010,4220,150,2120,240);
      List<Integer> list2= Arrays.asList(2300,2200,1020,2232,380,1200,350);
      jsonObject.put("music",list1);
      jsonObject.put("ticket",list2);
    }else if(type.equals("tasks")){
      List<Integer> list1= Arrays.asList(10220,201220,2222,42220,15220,2120,24220);
      List<Integer> list2= Arrays.asList(23200,220220,1022220,22232,380,1200,352220);
      jsonObject.put("music",list1);
      jsonObject.put("ticket",list2);
    }
    return jsonObject;
  }


  @RequestMapping(path="showPanelGroupData")
  public JSONObject showPanelGroupData(String id,String token){
    JSONObject jsonObject=new JSONObject();
    Map<String,Integer> datas=new HashMap<>();
    datas.put("end",234212);
    datas.put("duration",3600);
    jsonObject.put("datas",datas);


    Map<String,Integer> exceptions=new HashMap<>();
    exceptions.put("end",2212);
    exceptions.put("duration",2500);
    jsonObject.put("exceptions",exceptions);


    Map<String,Integer> processes=new HashMap<>();
    processes.put("end",112);
    processes.put("duration",3100);
    jsonObject.put("processes",processes);


    Map<String,Integer> tasks=new HashMap<>();
    tasks.put("end",212);
    tasks.put("duration",2200);
    jsonObject.put("tasks",tasks);


    return jsonObject;

  }
}
