package com.open.application.spider.spiders;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.open.application.spider.helper.ClientHelper;
import com.open.application.spider.models.TbAllMusic;
import com.open.application.spider.operations.MusicUpdateOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 获取歌词
 * @author HeYuanHao
 * @date 2019/1/15 下午1:29
 */
@Slf4j
@Service
public class AllLyricSpider {

  private static final String lyricHref = "http://music.163.com/api/song/lyric?os=pc&id=%s&lv=-1&kv=-1&tv=-1";

  private String getAllLyric(String url) throws IOException {
    String json = ClientHelper.get(url);
    JSONObject jsonObject = JSON.parseObject(json);
    String lyric = jsonObject.getJSONObject("lrc").getString("lyric").replaceAll("\\[.*?\\]", "");
    return lyric;
  }

  public void getAllLyric(List<Long> songIds, MusicUpdateOperation operation) {
    for (Long songId : songIds) {
      String href = String.format(lyricHref, songId);
      String lyric = null;
      try {
        lyric = getAllLyric(href);
        log.info("spider for lyric execute success,song id is {}, lyric is \n{}", songId, lyric);
      } catch (IOException e) {
        log.error("spider for lyric error, song id is {}", songId, e);
      }
      if (StringUtils.isNotBlank(lyric)) {
        operation.update(TbAllMusic.builder().songid(songId).lyric(lyric).build());
      }
    }

  }
}
