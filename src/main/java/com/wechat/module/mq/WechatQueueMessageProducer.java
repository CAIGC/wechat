package com.wechat.module.mq;

import com.alibaba.fastjson.JSONObject;
import org.apache.activemq.command.ActiveMQDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by CAI_GC on 2016/12/22.
 */
@Component
public class WechatQueueMessageProducer {
    private static JmsTemplate jmsTemplate;
    private static ActiveMQDestination destination;

    @Resource(name="wechatQueue")
    public  void setDestination(ActiveMQDestination destination) {
        WechatQueueMessageProducer.destination = destination;
    }
    @Autowired
    public  void setJmsTemplate(JmsTemplate jmsTemplate) {
        WechatQueueMessageProducer.jmsTemplate = jmsTemplate;
    }

    public static void send(Object object) {
        jmsTemplate.convertAndSend(destination, JSONObject.toJSONString(object));
    }
}
