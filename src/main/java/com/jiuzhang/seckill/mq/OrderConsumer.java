package com.jiuzhang.seckill.mq;

import com.alibaba.fastjson.JSON;
import com.jiuzhang.seckill.db.dao.SeckillOrderDao;
import com.jiuzhang.seckill.db.po.SeckillOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
@RocketMQMessageListener(topic = "seckill_order", consumerGroup = "seckill_order_group")
public class OrderConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    private SeckillOrderDao seckillOrderDao;
    @Override
    public void onMessage(MessageExt messageExt) {
        String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("order info:{}", message);
        SeckillOrder order = JSON.parseObject(message, SeckillOrder.class);
        order.setCreateTime(new Date());
        // 锁定库存
        boolean lockStockResult = seckillOrderDao.lockStock(order.getSeckillActivityId());
        if(lockStockResult){
            order.setOrderStatus(1);
        }else {
            order.setOrderStatus(0);
        }
        seckillOrderDao.insertOrder(order);
    }
}
