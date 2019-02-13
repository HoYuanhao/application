package com.open.application.job;

import com.open.application.common.models.Task;
import com.open.application.common.util.EsUtil;
import com.open.application.dao.TaskJobDao;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author HeYuanHao
 * @date 2019/1/21 下午2:11
 */
@Slf4j
@JobHandler("updateTaskStatusJob")
@Component
public class UpdateTaskStatusJob extends IJobHandler {
  @Autowired
  private TaskJobDao taskJobDao;
  @Autowired
  private RedisTemplate<String,String> redisTemplate;
  @Autowired
  private RestHighLevelClient client;
  @Override
  public ReturnT<String> execute(String s) throws Exception {
    List<Task> tasks=taskJobDao.selectRunTask();
    tasks.forEach(task->{
      String tid=task.getTid();
      boolean flag=redisTemplate.hasKey("status_"+tid);
      if(!flag){
        taskJobDao.updateTaskStatus(tid,"0");
        taskJobDao.updateTaskEndTime(tid,new Date(System.currentTimeMillis()-60000));
        try {
          UpdateByQueryRequest queryRequest=new UpdateByQueryRequest(EsUtil.getTaskIndex(task.getUid()))
            .setQuery(QueryBuilders.termQuery("tid",tid)).setBatchSize(1).setDocTypes("_doc").setRefresh(true).setScript(new Script(
              "ctx._source.status = 0"));
          client.updateByQuery(queryRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
          log.error("es update error");
        }
      }
    });

    return new ReturnT(ReturnT.SUCCESS_CODE,String.valueOf(tasks.size()));
  }
}
