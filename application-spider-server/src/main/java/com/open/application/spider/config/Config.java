package com.open.application.spider.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.open.application.common.util.EsUtil;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author HeYuanHao
 * @date 2019/1/17 下午2:56
 */
@Configuration
public class Config {
  @Value("${elasticsearch.address}")
  private String address;
  @Value("${elasticsearch.scheme}")
  private String scheme;
  @Value("${spring.redis.host}")
  private String host;
  @Value("${spring.redis.port}")
  private int port;
  @Value("${spring.redis.timeout}")
  private int timeout;



  @Bean
  public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory){
    StringRedisTemplate template = new StringRedisTemplate(factory);
    setSerializer(template);
    template.afterPropertiesSet();
    return template;
  }

  private void setSerializer(StringRedisTemplate template){
    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
    ObjectMapper om = new ObjectMapper();
    jackson2JsonRedisSerializer.setObjectMapper(om);
    template.setValueSerializer(jackson2JsonRedisSerializer);
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

  }

  @Bean
  public ThreadPoolExecutor threadPoolExecutor() {
    ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("spider-job-server-thread-%d").build();
    return new ThreadPoolExecutor(20, 120, 120, TimeUnit.SECONDS,new ArrayBlockingQueue<>(20), threadFactory);
  }

  @Bean
  public RestHighLevelClient restHighLevelClient(){
    return new RestHighLevelClient(RestClient.builder(EsUtil.getHttpHost(address, scheme)));
  }

}
