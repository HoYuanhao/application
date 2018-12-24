package com.open.application.console.server.es.service.impl;

import com.open.application.common.models.Task;
import com.open.application.console.server.es.service.ElasticSearchService;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ElasticSearchServiceImpl implements ElasticSearchService {

@Value("${elasticsearch.address}")
private String address;
@Value("${elasticsearch.port}")
private Integer port;

private RestHighLevelClient client;

@PostConstruct
public void init(){
  client=new RestHighLevelClient(RestClient.builder());
}


  @Override
  public List<Task> searchTaskByLikeNameAndDescribe(String index, String uid, Integer offset,
      Integer limit, String name, String describe) {
    return null;
  }
}
