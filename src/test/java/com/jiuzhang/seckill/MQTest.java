package com.jiuzhang.seckill;

import com.jiuzhang.seckill.mq.RocketMQService;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class MQTest {
    @Autowired
    private RocketMQService rocketMQService;

    @Test
    public void testSendMQ() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        rocketMQService.sendMessage("test-jiuzhang", "hello, world!"+ new Date().toString());
    }
}
