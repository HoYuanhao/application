package com.open.application.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.forcemerge.ForceMergeRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author HeYuanHao
 * @date 2019/1/17 下午2:21
 */
@Slf4j
@Component
@JobHandler(value="esForceMergeJob")
public class EsForceMergeJob extends IJobHandler {
  @Autowired
  private RestHighLevelClient client;

  @Override
  public ReturnT<String> execute(String s) throws Exception {
    try {
      ForceMergeRequest request = new ForceMergeRequest("*").flush(true).maxNumSegments(1).onlyExpungeDeletes(true);
      client.indices().forcemerge(request, RequestOptions.DEFAULT);
    } catch (Exception e) {
      log.error("deleteEsJob error", e);
      return new ReturnT<>(ReturnT.FAIL_CODE, "deleteEsJob error:\n" + e.toString());
    }
    return ReturnT.SUCCESS;
  }
}
