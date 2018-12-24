package com.open.application.console.controller;

import com.alibaba.fastjson.JSONObject;
import com.open.application.common.models.Task;
import com.open.application.common.service.TaskShowService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
    taskList = taskList.parallelStream().sorted((task1, task2) -> {
      if (task1.getCreateTime().after(task2.getCreateTime())) {
        return -1;
      } else if (task1.getCreateTime().before(task2.getCreateTime())) {
        return 1;
      } else {
        return 0;
      }
    }).collect(Collectors.toList());
    Integer total = taskShowService.countTaskDataByUid(id);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("tasks", taskList);
    jsonObject.put("total", total);
    return jsonObject;
  }

  @RequestMapping(path = "searchTask", method = RequestMethod.GET)
  public Map searchTask(String id, String token, String name, String describe,
      @RequestParam(value = "limit", defaultValue = "20") Integer limit,
      @RequestParam("page") Integer page) {
    Map<String,Object> result=taskShowService.searchTaskByNameOrDescribeAndByUid(id,limit*(page-1),limit,name,describe);
    return result;
  }

  @RequestMapping(path = "newTask",method = RequestMethod.POST)
  public JSONObject newTask(@RequestBody Task task){
      task.setCreateTime(new Date());
      task.setTid(UUID.randomUUID().toString().replace("-",""));
      taskShowService.insertTaskToElasticSearchAndDB(task);
      JSONObject jsonObject=new JSONObject();
      jsonObject.put("status",200);
      return jsonObject;
  }
}
