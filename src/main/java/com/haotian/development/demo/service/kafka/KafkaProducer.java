package com.haotian.development.demo.service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * 生产者
 * 使用@EnableScheduling注解开启定时任务
 */
@Component
public class KafkaProducer {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;




}
