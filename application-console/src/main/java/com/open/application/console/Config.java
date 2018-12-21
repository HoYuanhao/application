package com.open.application.console;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import java.nio.charset.Charset;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class Config {
  @Bean
  public FastJsonHttpMessageConverter fastJsonHttpMessageConverter(){
    FastJsonHttpMessageConverter fastJsonHttpMessageConverter=new FastJsonHttpMessageConverter();
    FastJsonConfig fastJsonConfig=new FastJsonConfig();
    fastJsonConfig.setCharset(Charset.forName("utf-8"));
    fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
    return fastJsonHttpMessageConverter;
  }

  @Bean
  public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
    ThreadPoolTaskScheduler threadPoolTaskScheduler=new ThreadPoolTaskScheduler();
    threadPoolTaskScheduler.setPoolSize(30);
    return threadPoolTaskScheduler;
  }
  private CorsConfiguration buildConfig() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.addAllowedOrigin("*");
    corsConfiguration.addAllowedHeader("*");
    corsConfiguration.addAllowedMethod("*");
    return corsConfiguration;
  }

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", buildConfig());
    return new CorsFilter(source);
  }
}
