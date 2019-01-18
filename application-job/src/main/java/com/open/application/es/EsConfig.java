package com.open.application.es;

import com.open.application.common.util.EsUtil;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HeYuanHao
 * @date 2019/1/17 下午2:12
 */
@Configuration
@Slf4j
public class EsConfig {

  @Value("${elasticsearch.address}")
  private String address;
  @Value("${elasticsearch.scheme}")
  private String scheme;
  @Value("${xxl.job.admin.addresses}")
  private String adminAddresses;

  @Value("${xxl.job.executor.appname}")
  private String appName;

  @Value("${xxl.job.executor.ip}")
  private String ip;

  @Value("${xxl.job.executor.port}")
  private int port;

  @Value("${xxl.job.accessToken}")
  private String accessToken;

  @Value("${xxl.job.executor.logpath}")
  private String logPath;

  @Value("${xxl.job.executor.logretentiondays}")
  private int logRetentionDays;


  @Bean(initMethod = "start", destroyMethod = "destroy")
  public XxlJobSpringExecutor xxlJobExecutor() {
    log.info(">>>>>>>>>>> xxl-job config init.");
    XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
    xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
    xxlJobSpringExecutor.setPort(port);
    xxlJobSpringExecutor.setAccessToken(accessToken);
    xxlJobSpringExecutor.setLogPath(logPath);
    xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
    xxlJobSpringExecutor.setAppName(appName);
    xxlJobSpringExecutor.setIp(ip);
    return xxlJobSpringExecutor;
  }

  @Bean
  public RestHighLevelClient restHighLevelClient(){
    return new RestHighLevelClient(RestClient.builder(EsUtil.getHttpHost(address, scheme)));
  }

}
