package com.open.application.console.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONPOJOBuilder;
import com.open.application.common.models.TbAllSinger;
import com.open.application.common.service.SingerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * @author HeYuanHao
 * @date 2019/1/17 下午6:51
 */
@Slf4j
@RequestMapping("singer")
@RestController
public class SingerController {
  @Autowired
  private SingerService singerService;

  @RequestMapping("getSingerLimit")
  @ResponseBody
  public JSONObject getAllSingerLimit(@RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                                      @RequestParam(value = "limit", defaultValue = "20", required = false) Integer limit,
                                      @RequestParam(value = "id") String id,
                                      @RequestParam(value = "token") String token) {
    List<TbAllSinger> list = singerService.getSingerLimit((page - 1) * limit, limit);
    int count = singerService.countSinger();
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("singers", list);
    jsonObject.put("count", count);
    return jsonObject;
  }

  @RequestMapping("searchSinger")
  @ResponseBody
  public JSONObject searchSinger(@RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                                 @RequestParam(value = "limit", defaultValue = "100", required = false) Integer limit,
                                 @RequestParam(value = "id") String id,
                                 @RequestParam(value = "token") String token,
                                 @RequestParam(value = "singerDesc",required = false) String singerDesc,
                                 @RequestParam(value = "singerName",required = false) String singerName,
                                 @RequestParam(value = "singerId",required = false)String singerId) {
      Map<String,Object> map=singerService.searchSinger(singerName, singerDesc, singerId, (page-1)*limit, limit);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("singers", map.get("singers"));
    jsonObject.put("count", map.get("count"));
    return jsonObject;

  }

}
