package com.open.application.console;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
@EnableDubbo
@EnableDubboConfig
public class App {

  public static void main(String[] args) {

    new SpringApplicationBuilder(App.class).run(args);
  }

}
