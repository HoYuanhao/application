package com.open.application.console.server;


import com.alibaba.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@MapperScan("com.open.application.console.server.dao")
@EnableDubboConfig
@SpringBootApplication
public class App {
  public static void main(String[] args){
    new SpringApplicationBuilder(App.class).run(args);
  }

}
