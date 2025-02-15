package com.jiuzhang.seckill.mq;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RocketMQMessageListener(topic = "test-jiuzhang" , consumerGroup = "conmuserGropjiuzhang")
public class ConsumerListener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt messageExt) {
        try {
            String msg = new String(messageExt.getBody(), "UTF-8");
            System.out.println(msg);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
