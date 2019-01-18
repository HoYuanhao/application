package com.open.application.console.server.config;


import com.open.application.common.util.EsUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HeYuanHao
 * @date 2019/1/16 上午10:50
 */
@Configuration
@Slf4j
public class Config {

  @Value("${elasticsearch.address}")
  private String address;
  @Value("${elasticsearch.scheme}")
  private String scheme;

  @Bean
  public RestHighLevelClient restHighLevelClient(){
  return new RestHighLevelClient(RestClient.builder(EsUtil.getHttpHost(address, scheme)));
  }

}
