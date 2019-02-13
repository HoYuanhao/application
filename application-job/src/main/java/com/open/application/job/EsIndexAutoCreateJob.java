package com.open.application.job;

import ch.qos.logback.core.util.FileUtil;
import com.google.common.io.Files;
import com.open.application.common.util.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.Charset;


/**
 * @author HeYuanHao
 * @date 2019/1/17 下午5:18
 */
@Slf4j
@Component
@JobHandler("esIndexAutoCreateJob")
public class EsIndexAutoCreateJob extends IJobHandler {
  @Autowired
  private RestHighLevelClient client;

  @Override
  public ReturnT<String> execute(String s) throws Exception {
    try {
      String date = DateUtil.getOneDayAfterNowString();
      CreateIndexRequest createSingerIndexRequest = new CreateIndexRequest("singer_" + date);
      CreateIndexRequest createExceptionIndexRequest = new CreateIndexRequest("exception_" + date);
      String singerIndexJson = Files.toString(new File(
        this.getClass().getClassLoader().getResource("").getPath() + "/tbAllSinger.json"), Charset.defaultCharset());
      String exceptionIndexJson = Files.toString(new File(
        this.getClass().getClassLoader().getResource("").getPath() + "/exception.json"), Charset.defaultCharset());
      createSingerIndexRequest.source(singerIndexJson, XContentType.JSON);
      createExceptionIndexRequest.source(exceptionIndexJson, XContentType.JSON);
      client.indices().create(createSingerIndexRequest, RequestOptions.DEFAULT);
      client.indices().create(createExceptionIndexRequest, RequestOptions.DEFAULT);
    } catch (Exception e) {
      log.error("job error", e);
      return ReturnT.FAIL;
    }
    return ReturnT.SUCCESS;
  }
}
