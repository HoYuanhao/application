package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.open.application.spider.dao")
@SpringBootApplication
@EnableTransactionManagement
public class App {
  public static void main(String[] args){
    new SpringApplicationBuilder(App.class).run(args);
  }

}
