package com.open.application.spider.spiders;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.open.application.spider.helper.ClientHelper;
import com.open.application.spider.models.TbAllMusic;
import com.open.application.spider.operations.MusicInsertOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 通过歌手的表去获取每个歌手的热门歌曲信息
 *
 * @author HeYuanHao
 * @date 2019/1/15 下午1:09
 */
@Slf4j
@Service
public class AllHotSongSpider {

  private static final String songUrl = "https://music.163.com/#/song?id=%s";
  private static final String outerUrl = "http://music.163.com/song/media/outer/url?id=%s.mp3";

  /**
   * 解析json数据
   *
   * @param url
   * @return
   * @throws IOException
   */
  private String getAllHotSong(String url) throws IOException {
    String result = ClientHelper.get(url);
    Document document = Jsoup.parse(result);
    Elements elements = document.select("#song-list-pre-data");
    String resJson = elements.text();
    if (resJson != null) {
      return resJson;
    } else {
      return null;
    }
  }

  /**
   * @param hrefs singerHrefs
   * @return 热门歌手热门歌曲
   */
  public void getAllHotSong(List<String> hrefs, MusicInsertOperation operation) {
    for (String href : hrefs) {
      String json = null;
      try {
        json = getAllHotSong(href);
        log.info(href + " spider execute success, href is {}, response is \n{}", href, json);
      } catch (IOException e) {
        log.error("spider for all hot song error,href is \n{}", href, e);
      }
      if (StringUtils.isNotBlank(json)) {
        JSONArray array = JSONObject.parseArray(json);
        for (int i = 0; i < array.size(); i++) {
          JSONObject object = array.getJSONObject(i);
          Long songId = object.getLong("id");
          String songName = object.getString("name");
          JSONArray array1 = object.getJSONArray("artists");
          JSONObject object2 = array1.getJSONObject(0);
          String singer = object2.getString("name");
          operation.insert(TbAllMusic
                             .builder()
                             .songid(songId)
                             .songname(songName)
                             .songsinger(singer)
                             .songurl(String.format(songUrl, songId))
                             .outerurl(String.format(outerUrl, songId))
                             .build());
        }
      }
    }

  }
}
