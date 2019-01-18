package com.open.application;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author HeYuanHao
 * @date 2019/1/17 下午2:12
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.open.application")
@MapperScan(basePackages = "com.open.application.dao")
public class App {
  public static void main(String[] args) {
    new SpringApplicationBuilder(App.class).run(args);
  }
}
