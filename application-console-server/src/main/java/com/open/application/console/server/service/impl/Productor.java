package com.open.application.console.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author HeYuanHao
 * @date 2019/1/15 下午4:13
 */
@Slf4j
@Component
public class Productor {
  @Value("${rocket.application.nameSrvAddr}")
  private String nameSrvAddr;
  @Value("${rocketmq.application.name}")
  private String name;
  private DefaultMQProducer mqProducer;

  @PostConstruct
  public void init(){
  mqProducer=new DefaultMQProducer(name);
  mqProducer.setNamesrvAddr(nameSrvAddr);
    try {
      mqProducer.start();
    } catch (MQClientException e) {
      e.printStackTrace();
    }
  }

  public SendResult sendMessage(Message message){
    try {
      return mqProducer.send(message);
    } catch (MQClientException e) {
      e.printStackTrace();
    } catch (RemotingException e) {
      e.printStackTrace();
    } catch (MQBrokerException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }


@PreDestroy
  public void destory(){
    if(mqProducer!=null) {
      mqProducer.shutdown();
    }
}
}
