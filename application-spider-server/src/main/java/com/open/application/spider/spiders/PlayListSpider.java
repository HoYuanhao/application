package com.open.application.spider.spiders;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.open.application.spider.helper.ClientHelper;
import com.open.application.spider.models.TbAllMusic;
import com.open.application.spider.models.TbPlaylist;
import com.open.application.spider.operations.MusicInsertOperation;
import com.open.application.spider.operations.PlayInsertOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.assertj.core.util.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取歌单
 *
 * @author HeYuanHao
 * @date 2019/1/15 下午2:44
 */
@Slf4j
@Service
public class PlayListSpider {
  private static final String outerUrlTemp = "http://music.163.com/song/media/outer/url?id=%s.mp3";

  /**
   * 获取歌单里面的歌曲数据
   *
   * @param url 接口的链接
   * @return 返回json的数据
   * @throws IOException
   */
  public ArrayList<String> getSong(String url) throws IOException {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpGet httpget = new HttpGet(url);
    httpget.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
    httpget.addHeader("Host", "music.163.com");
    CloseableHttpResponse response = httpclient.execute(httpget);
    HttpEntity entity = response.getEntity();
    String html = EntityUtils.toString(entity);
    Document document = Jsoup.parse(html);
    ArrayList<String> bq = Lists.newArrayList();
    Elements element = document.select("ul[class=f-hide] a");
    for (Element element1 : element) {
      long songId = Long.parseLong(element1.attr("href").replace("/song?id=", ""));
      bq.add("http://music.163.com/api/song/detail/?id=" + songId + "&ids=%5B+" + songId + "%5D");
    }
    return bq;
  }


  /**
   * 获取热门歌单的数据
   *
   * @param url https://music.163.com/discover/playlist/?order=new&cat=%E5%85%A8%E9%83%A8&limit=35&offset=" + i * 35
   * @throws IOException
   */
  public static void getPlayList(String url, PlayInsertOperation operation) throws IOException {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpGet httpget = new HttpGet(url);
    httpget.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
    httpget.addHeader("Host", "music.163.com");
    CloseableHttpResponse response = httpclient.execute(httpget);
    HttpEntity entity = response.getEntity();
    String html = EntityUtils.toString(entity);
    Document document = Jsoup.parse(html);
    Elements element = document.select("#m-pl-container > li > div");
    for (Element element1 : element) {
      Elements element2 = element1.select("a");
      Elements element3 = element1.select("img");

      operation.insert(TbPlaylist.builder().ishot(0).listtitle(element2.attr("title")).photohref(element3.attr("src")).listhref(
        "https://music.163.com" + element2.attr("href")).playlistid(Long.parseLong(element2.attr("href").replace("/playlist?id=", ""))).build());

    }
  }

  /**
   * 通过专辑列表获取歌曲信息
   *
   * @param tbPlaylists Playlistid  Listhref 必须有
   * @param operation
   */
  public void insertMusicByPlayList(List<TbPlaylist> tbPlaylists, MusicInsertOperation operation) {
    for (TbPlaylist tbPlaylist : tbPlaylists) {
      ArrayList<String> arrayList = null;
      try {
        arrayList = getSong(tbPlaylist.getListhref());
      } catch (IOException e) {
        log.error("getSong by play list error, url is {}", tbPlaylist.getListhref());
      }

      for (String url : arrayList) {
        if (StringUtils.isNotBlank(url)) {
          Long songId = Long.parseLong(url.substring(url.indexOf("id=") + 3, url.indexOf("&")));
          String source = null;
          try {
            source = ClientHelper.get(url);
          } catch (IOException e) {
            log.error("get song error url is {}", url, e);
          }
          if (StringUtils.isNotBlank(source) && source.length() > 200) {
            JSONObject jsonObject = JSON.parseObject(source);
            JSONArray jsonArray = jsonObject.getJSONArray("songs");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            String songName = jsonObject1.getString("name");
            JSONArray jsonArray1 = jsonObject1.getJSONArray("artists");
            JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
            String songSinger = jsonObject2.getString("name");
            if (!songName.equals("错误引导")) {
              operation.insert(TbAllMusic.builder().songid(songId).songname(songName).songsinger(songSinger).songurl(
                "https://music.163.com/#/song?id=" + songId).outerurl(String.format(outerUrlTemp, songId)).build());
            }
          }

        }
      }

    }
  }
}
