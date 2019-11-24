package com.example.controller;

import com.example.config.JmsConfig;
import com.example.mq.Consumer;
import com.example.mq.Producer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MqTestController {
    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private Producer producer;

    private List<String> mesList;

    @RequestMapping("/text/rocketmq")
    public Object callback() throws Exception {
        for(int i=0;i<=10000;i++){
            Thread.sleep(1000);
            //创建生产信息
            Message message = new Message(JmsConfig.TOPIC, "testtag", ("测试MQ:" + i).getBytes());
            //发送
            SendResult sendResult = producer.getProducer().send(message);
            log.info("输出生产者信息={}",sendResult);
        }

        return "成功";
    }
}
