package com.open.application.console.controller;

import com.alibaba.fastjson.JSONObject;
import com.open.application.common.models.ExceptionModel;
import com.open.application.common.models.Task;
import com.open.application.common.service.ExceptionService;
import com.open.application.common.service.TaskShowService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

/**
 * 任务显示与插入控制器
 *
 * @author HeYuanHao
 */
@RestController
@Slf4j
@RequestMapping("/task")
public class TaskShowController {

  @Autowired
  private TaskShowService taskShowService;
  @Autowired
  private ExceptionService exceptionService;

  @RequestMapping(path = "/showTaskData", method = RequestMethod.GET)
  public JSONObject showTaskData(String id,
                                 String token,
                                 @RequestParam(value = "limit", defaultValue = "20") Integer limit,
                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "showrun") boolean showrun) {
    List<Task> taskList = taskShowService.showTaskDataByUid(id, limit * (page - 1), limit,showrun);
    taskList = taskList.parallelStream().sorted((task1, task2) -> {
      if (task1.getCreateTime().after(task2.getCreateTime())) {
        return -1;
      } else if (task1.getCreateTime().before(task2.getCreateTime())) {
        return 1;
      } else {
        return 0;
      }
    }).collect(Collectors.toList());
    Integer total = taskShowService.countTaskDataByUid(id,showrun);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("tasks", taskList);
    jsonObject.put("total", total);
    return jsonObject;
  }

  @RequestMapping(path = "/searchTask", method = RequestMethod.GET)
  public Map searchTask(String id,
                        String token,
                        @RequestParam(value = "name", required = false) String name,
                        @RequestParam(value = "describe", required = false) String describe,
                        @RequestParam(value = "limit", defaultValue = "20") Integer limit,
                        @RequestParam("page") Integer page,
                        @RequestParam(value = "status", required = false) Integer status,
                        @RequestParam(value = "type", required = false) String type) {
    Map<String, Object> result = taskShowService.searchTaskByNameOrDescribeAndByUid(id, limit * (page - 1), limit, name, describe, status, type);
    return result;
  }

  @RequestMapping(path = "/newTask", method = RequestMethod.POST)
  public JSONObject newTask(@RequestBody Task task) {
    task.setCreateTime(new Date());
    task.setStatus(0);
    task.setTid(UUID.randomUUID().toString().replace("-", ""));
    task.setSource(task.getSource().replace(" ", ""));

      taskShowService.insertTaskToElasticSearchAndDB(task);

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("status", 200);
    return jsonObject;
  }

  @RequestMapping(path = "/getTaskIDs", method = RequestMethod.GET)
  public List getTaskIDs(String id, String token) {
    List<String> taskIDS = taskShowService.getTaskIDs(id);
    log.info(taskIDS.toString());
    return taskIDS;
  }

  @RequestMapping(path = "/q", method = RequestMethod.GET)
  public List getExceptionType(String id, String token, String tid) {
    List<String> types = taskShowService.getExceptionTypeByTidAndUid(tid, id);
    return types;
  }

  @RequestMapping(path = "/s", method = RequestMethod.GET)
  public Map searchExceptionType(String tid,
                                 String id,
                                 String token,
                                 @RequestParam(value = "type", required = false) String type,
                                 @RequestParam(value = "key", required = false) String key,
                                 @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                 @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
    //    Map<String, Object> map = exceptionService.searchExceptions(id, tid, type, key, offset, limit);
    Map<String, Object> map = new HashMap<>();
    map.put("totalHits", 20);
    List<ExceptionModel> list = new ArrayList<ExceptionModel>();
    map.put("exceptions", list);
    for (int i = 0; i < 10; i++) {
      try {
        int z = 1 / 0;
      } catch (Exception e) {

        list.add(ExceptionModel
                   .builder()
                   .uid(id)
                   .type(e.getClass().getTypeName())
                   .tid(tid)
                   .throwTime(new Date())
                   .detail(e.toString())
                   .pid("b6852c97119c41a8a519eeaae50f10f7")
                   .eid(UUID.randomUUID().toString().replace("-", ""))
                   .build());
      }
    }
    return map;
  }

}
