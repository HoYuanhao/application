package com.open.application.spider.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.Uninterruptibles;
import com.open.application.spider.dao.SingerDao;
import com.open.application.spider.elasticsearch.ElasticSearchService;
import com.open.application.spider.spiders.AllSingerSpider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author HeYuanHao
 * @date 2019/1/17 下午2:51
 */
@Component
@Slf4j
public class SpiderConsumer implements ApplicationListener<ContextRefreshedEvent> {
  private static final String SPIDER_SINGER = "SINGER";
  @Value("${rocketmq.application.name}")
  private String consumerGroup;
  @Value("${rocket.application.nameSrvAddr}")
  private String nameSrvAddr;
  @Value("${rocket.application.topic}")
  private String topic;
  @Value("${rocket.consumeMessageBatchMaxSize}")
  private Integer consumeMessageBatchMaxSize;
  @Value("${rocket.consumeThreadMax}")
  private Integer consumeThreadMax;
  private DefaultMQPushConsumer defaultMQPushConsumer;
  @Autowired
  private ThreadPoolExecutor executor;
  @Autowired
  private RedisTemplate<String, String> redisTemplate;
  @Autowired
  private AllSingerSpider singerSpider;
  @Autowired
  private SingerDao singerDao;
  @Autowired
  private ElasticSearchService elasticSearchService;

  @PostConstruct
  public void init() {
    defaultMQPushConsumer = new DefaultMQPushConsumer(consumerGroup);
    defaultMQPushConsumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
    defaultMQPushConsumer.setConsumeThreadMax(consumeThreadMax);
    defaultMQPushConsumer.setNamesrvAddr(nameSrvAddr);
    defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
    try {
      defaultMQPushConsumer.subscribe(topic, "*");
    } catch (MQClientException e) {
      log.error("subscribe topic error!", e);
    }
    defaultMQPushConsumer.registerMessageListener((MessageListenerOrderly) (messages, context) -> {
      messages.forEach(msg -> {
        //如果获得线程等于线程池最大线程，那么等待5秒，等候其他线程执行完毕
        while (executor.getActiveCount() == executor.getMaximumPoolSize()) {
          log.info("sleep 5s");
          Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
        }
        //得到msg
        String body = new String(msg.getBody(), Charset.defaultCharset());
        log.info("body is {}", body);
        final JSONObject jsonObject = JSON.parseObject(body);
        String tid = jsonObject.getString("tid");
        Integer processNum = jsonObject.getInteger("processNum");
        String sourceString = jsonObject.getString("source");
        if (!StringUtils.isBlank(sourceString)) {
          JSONObject source = JSON.parseObject(sourceString);
          String type = source.getString("type");
          if (type.equalsIgnoreCase(SPIDER_SINGER)) {
            JSONArray jsonArray = source.getJSONArray("initialArray");
            Set<Character> set = Sets.newHashSet();
            if (jsonArray != null) {
              jsonArray.forEach(s -> set.add(((String) s).toUpperCase().charAt(0)));
            }
            String initials = source.getString("initials").toUpperCase();
            if (!StringUtils.isBlank(initials)) {
              char start = initials.charAt(0);
              char end = initials.charAt(2);
              for (int i = start; i <= end; i++) {
                set.add((char) i);
              }
            }
            int size = set.size();
            //processNum过大自动缩容
            if (processNum > size) {
              processNum = size;
            }
            int tmp = size / processNum;
            //启动processNum数量的线程
            List<Character> characterList = Lists.newArrayList(set);
            for (int i = 0; i < processNum; i++) {
              Set<Character> characters = Sets.newHashSet();
              if (i == processNum - 1) {
                for (int j = i * tmp; j < size; j++) {
                  characters.add(characterList.get(j));
                }
              } else {
                for (int j = i * tmp; j < (i + 1) * tmp; j++) {
                  characters.add(characterList.get(j));
                }
              }
              executor.execute(() -> {
                Uninterruptibles.sleepUninterruptibly((long)(Math.random()*5000),TimeUnit.MILLISECONDS);
                String status = redisTemplate.opsForValue().get("status_" + tid);
                if (!StringUtils.isBlank(status)) {
                  singerSpider.getMandopopSinger(characters, singer -> {
                    log.info(JSONObject.toJSONString(singer));
                    singer.setGetTime(new Date());
                    elasticSearchService.insertSingerToEs(singer);
                    singerDao.insertTbAllSinger(singer);
                  });
                  redisTemplate.expire("status_" + tid, 30, TimeUnit.DAYS);

                }
              });
            }
          }
        }
      });
      return ConsumeOrderlyStatus.SUCCESS;
    });


  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    if (defaultMQPushConsumer != null) {
      try {
        defaultMQPushConsumer.start();
      } catch (MQClientException e) {
        log.error("consumer start error", e);
      }
    }
  }

  @PreDestroy
  public void destroy() {
    if (defaultMQPushConsumer != null) {
      defaultMQPushConsumer.shutdown();
    }
  }
}
