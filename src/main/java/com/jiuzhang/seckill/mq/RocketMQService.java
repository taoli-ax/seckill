package com.jiuzhang.seckill.mq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RocketMQService {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendMessage(String topic, String msg) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Message message = new Message(topic, msg.getBytes());
        rocketMQTemplate.getProducer().setSendMsgTimeout(60000); // 设置超时时间
        rocketMQTemplate.getProducer().send(message);
    }

    public void sendDelayMessage(String topic, String msg, int delayTimeLevel) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Message message = new Message(topic, msg.getBytes());
        message.setDelayTimeLevel(delayTimeLevel);
        rocketMQTemplate.getProducer().send(message);
    }
}
