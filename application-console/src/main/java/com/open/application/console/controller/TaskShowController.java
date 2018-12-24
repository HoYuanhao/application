package com.open.application.console.controller;

import com.alibaba.fastjson.JSONObject;
import com.open.application.common.models.Task;
import com.open.application.common.service.TaskShowService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/task")
public class TaskShowController {

  @Autowired
  private TaskShowService taskShowService;

  @RequestMapping(path = "/showTaskData", method = RequestMethod.GET)
  public JSONObject showTaskData(String id, String token,
      @RequestParam(value = "limit", defaultValue = "20") Integer limit,
      @RequestParam(value = "page", defaultValue = "1") Integer page) {
    List<Task> taskList = taskShowService.showTaskDataByUid(id, limit * (page - 1), limit);
    taskList=taskList.parallelStream().sorted((task1,task2)->{
    if(task1.getCreateTime().after(task2.getCreateTime())){
      return -1;
    }else if(task1.getCreateTime().before(task2.getCreateTime())){
      return 1;
    }else{
      return 0;
    }
    }).collect(Collectors.toList());
    Integer total=taskShowService.countTaskDataByUid(id);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("tasks", taskList);
    jsonObject.put("total",total);
    return jsonObject;
  }


}
