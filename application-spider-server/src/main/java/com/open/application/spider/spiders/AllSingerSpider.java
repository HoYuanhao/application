package com.open.application.spider.spiders;

import com.open.application.common.models.TbAllSinger;
import com.open.application.spider.helper.ClientHelper;
import com.open.application.spider.operations.SingerInsertOperation;
import com.open.application.spider.operations.SingerUpdateOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.internal.Characters;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 爬取所有的歌手信息 不包括日韩。
 *
 * @author HeYuanHao
 * @date 2019/1/15 下午1:54
 */
@Slf4j
@Service
public class AllSingerSpider {
  private static final String singUrl = "https://music.163.com/discover/artist/cat?id=1001&initial=%s";
  private static final String singerUrl = "https://music.163.com/artist/desc?id=%s";

  /**
   * 获取所有的歌手
   *
   * @param url
   * @throws IOException
   */
  public void getAllSinger(String url, SingerInsertOperation operation) {
    String result = null;
    try {
      result = ClientHelper.get(url);
    } catch (IOException e) {
      log.error("sing spider execute error, url is {}", url, e);
    }
    if (StringUtils.isNotBlank(result)) {
      Document document = Jsoup.parse(result);
      Elements elements = document.select("#m-artist-box > li > a.nm.nm-icn.f-thide.s-fc0");
      for (Element elements1 : elements) {
        long singerId=Long.parseLong(elements1.attr("href").replace("/artist?id=", ""));
        String singHref = elements1.attr("href");
        TbAllSinger tbAllSinger=TbAllSinger
          .builder()
          .singerHref("https://music.163.com" + singHref)
          .singerName(elements1.text())
          .singerId(singerId)
          .build();
        updateSinger(singerId,s->tbAllSinger.setSingerDesc(s));
        operation.insert(tbAllSinger);
      }

      Elements elements1 = document.select("#m-artist-box > li > div > a");
      for (Element element : elements1) {
        String singHref = element.attr("href");
        long singerId=Long.parseLong(element.attr("href").replace("/artist?id=", ""));
        TbAllSinger tbAllSinger=TbAllSinger
          .builder()
          .singerHref("https://music.163.com" + singHref)
          .singerName(element.attr("title").replace("的音乐", ""))
          .singerId(singerId)
          .build();
        updateSinger(singerId,s->tbAllSinger.setSingerDesc(s));
        operation.insert(tbAllSinger);
      }
    }
  }

  /**
   * 获取歌手介绍
   *
   * @param url
   * @return
   * @throws IOException
   */
  public String getSingerInfo(String url) throws IOException {
    String result = ClientHelper.get(url);
    Document document = Jsoup.parse(result);
    Elements elements = document.select("div.g-bd4.f-cb > div.g-mn4 > div > div > div:nth-child(3) > div");
    String str = null;
    for (Element element : elements) {
      str = element.toString();
    }
    return str;
  }

  /**
   * 获取华语男歌手信息
   *
   * @param initials A-Z
   */
  public void getMandopopSinger(Set<Character> initials, SingerInsertOperation operation) {
    for (Character character : initials) {
      char value = character.charValue();
      if ('A' <= value && value <= 'Z') {
        getAllSinger(String.format(singUrl, (int) value), operation);
      } else {
        continue;
      }
    }
  }

  public void updateSinger(Long singerId, SingerUpdateOperation operation) {
      String singerInfo = null;
      try {
        singerInfo = getSingerInfo(String.format(singerUrl, singerId));
      } catch (IOException e) {
        log.error("updateSinger error singer id is {}", singerId, e);
      }
      if (StringUtils.isNotBlank(singerInfo)) {
        operation.update(singerInfo);
      }




  }


}
