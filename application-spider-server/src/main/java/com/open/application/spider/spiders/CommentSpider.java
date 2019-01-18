package com.open.application.spider.spiders;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.open.application.common.models.TbComments;
import com.open.application.spider.helper.EncryptUtils;
import com.open.application.spider.operations.CommentInsertOperation;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author HeYuanHao
 * @date 2019/1/15 下午3:18
 */
@Service
public class CommentSpider {

  /**
   * 获取评论
   *
   * @param songId   歌曲的id
   * @param pageFrom 从第这一页开始爬
   * @param pageEnd  爬到这一页
   * @throws Exception
   */
  public static void getComment(Long songId, int pageFrom, int pageEnd, CommentInsertOperation operation) throws Exception {
    for (int i = pageFrom; i <= pageEnd; i++) {
      Thread.sleep(3000 + (i % 2) * 1000);
      System.out.println(Thread.currentThread().getName() + i);
      String secKey = new BigInteger(100, new SecureRandom()).toString(32).substring(0, 16);
      String encText = EncryptUtils.aesEncrypt(EncryptUtils.aesEncrypt("{\"offset\":" + (i - 1) * 20 + ",\"limit\":" + 20 + "};", "0CoJUm6Qyw8W8jud"), secKey);
      String encSecKey = EncryptUtils.rsaEncrypt(secKey);
      Connection.Response execute = Jsoup
        .connect("http://music.163.com/weapi/v1/resource/comments/R_SO_4_" + songId + "?csrf_token=")
        .header("Referer", "http://music.163.com/song?id=" + songId)
        .header("Cookie", "_ntes_nnid=b006c3a77c3852ebd7d6a8c1e1d140c4,1543164959783; _ntes_nuid=b006c3a77c3852ebd7d6a8c1e1d140c4; hb_MA-BFF5-63705950A31C_source=www.google.com.hk; nts_mail_user=15014537746@163.com:-1:1; locale=; mail_psc_fingerprint=3232bb94da41b0d580765b7934b21539; usertrack=CrHthVwK7EclAcmhAxcbAg==")
        .data("params", encText)
        .data("encSecKey", encSecKey)
        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
        .header("Cache-Control", "no-cache")
        .header("Accept", "*/*")
        .header("Accept-Ecoding", "gzip, deflate")
        .header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
        .header("Connection", "keep-alive")
        .header("Host", "music.163.com")
        .header("Content-Type", "application/x-www-form-urlencoded")
        .method(Connection.Method.POST)
        .ignoreContentType(true)
        .timeout(600000)
        .execute();
      String string = execute.body();
      JSONObject jsonObject = JSONObject.parseObject(string);
      int total = jsonObject.getInteger("total");

      //如果有热评的字段，则解析热评的字段
      if (jsonObject.containsKey("hotComments")) {
        JSONArray jsonArray = jsonObject.getJSONArray("hotComments");
        //解析热评
        for (int j = 0; j < jsonArray.size(); j++) {
          JSONObject j3 = jsonArray.getJSONObject(j);
          JSONObject j4 = j3.getJSONObject("user");

          operation.insert(TbComments
                             .builder()
                             .comment(j3.getString("content"))
                             .commentId(j3.getLong("commentId"))
                             .time(j3.getLong("time"))
                             .isHotComments(1)
                             .songId(songId)
                             .likedCount(j3.getInteger("likedCount"))
                             .avatarUrl(j4.getString("avatarUrl"))
                             .userId(j4.getLong("userId"))
                             .nickName(j4.getString("nickname"))
                             .commentNum(total)
                             .build());

        }
        //解析第一页的评论
        Analytical(jsonObject, songId, total, operation);
      } else {
        //解析第一页后的评论
        Analytical(jsonObject, songId, total, operation);
      }
    }
  }

  /**
   * 解析非热评的评论
   *
   * @param object 评论，json格式数据
   * @param songId 歌曲的id
   * @param total  评论的总数
   */
  public static void Analytical(JSONObject object, Long songId, int total, CommentInsertOperation operation) {
    //解析第一页的评论
    JSONArray jsonArray = object.getJSONArray("comments");
    for (int k = 0; k < jsonArray.size(); k++) {
      JSONObject j3 = jsonArray.getJSONObject(k);
      JSONObject j4 = j3.getJSONObject("user");
      operation.insert(TbComments
                         .builder()
                         .comment(j3.getString("content"))
                         .commentId(j3.getLong("commentId"))
                         .time(j3.getLong("time"))
                         .isHotComments(1)
                         .songId(songId)
                         .likedCount(j3.getInteger("likedCount"))
                         .avatarUrl(j4.getString("avatarUrl"))
                         .userId(j4.getLong("userId"))
                         .nickName(j4.getString("nickname"))
                         .commentNum(total)
                         .build());
    }
  }
}
