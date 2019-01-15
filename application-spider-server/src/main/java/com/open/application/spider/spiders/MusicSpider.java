package com.open.application.spider.spiders;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.open.application.spider.helper.ClientHelper;
import com.open.application.spider.models.TbAllMusic;
import com.open.application.spider.operations.MusicInsertOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author HeYuanHao
 * @date 2019/1/15 下午2:28
 */
@Slf4j
@Service
public class MusicSpider {
  private static final String songDetail = "http://music.163.com/api/song/detail/?id=%5B+%s&ids=%s%5D";
  private static final String outerUrlTemp = "http://music.163.com/song/media/outer/url?id=%s.mp3";

  public void spiderMusic(List<Long> songIds, MusicInsertOperation operation) {
    for (Long songId : songIds) {
      String infoUrl = String.format(songDetail, songId, songId);
      String source = null;
      try {
        source = ClientHelper.get(infoUrl);
      } catch (IOException e) {
        log.error("song info url request error songId is {}", songId, e);
      }

      if (StringUtils.isNotBlank(source) && source.length() > 200) {
        JSONObject jsonObject = JSON.parseObject(source);
        JSONArray jsonArray = jsonObject.getJSONArray("songs");
        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
        String songName = jsonObject1.getString("name");
        JSONArray jsonArray1 = jsonObject1.getJSONArray("artists");
        JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
        String songSinger = jsonObject2.getString("name");
        String url = "https://music.163.com/#/song?id=" + songId;
        if (!songName.equals("错误引导")) {
        operation.insert(TbAllMusic
                           .builder()
                           .songid(songId)
                           .songname(songName)
                           .songsinger(songSinger)
                           .songurl(url)
                           .outerurl(String.format(outerUrlTemp, songId))
                           .build());
        }
      }


    }

  }
}
