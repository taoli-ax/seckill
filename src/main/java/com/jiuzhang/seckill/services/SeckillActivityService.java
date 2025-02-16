package com.jiuzhang.seckill.services;

import com.alibaba.fastjson.JSON;
import com.jiuzhang.seckill.db.dao.SeckillActivityDao;
import com.jiuzhang.seckill.db.po.SeckillActivity;
import com.jiuzhang.seckill.db.po.SeckillOrder;
import com.jiuzhang.seckill.mq.RocketMQService;
import com.jiuzhang.seckill.util.RedisService;
import com.jiuzhang.seckill.util.SnowFlake;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeckillActivityService {
    @Autowired
    private RedisService redisService;

    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Autowired
    private RocketMQService rocketMQService;

    private SnowFlake snowFlake = new SnowFlake(1, 1);

    public SeckillOrder createSeckillOrder(long userId, Long activityId) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        /**
         * 创建Order
         */
        SeckillActivity activity = seckillActivityDao.querySeckillActivityById(activityId);
        SeckillOrder order = new SeckillOrder();
        order.setUserId(userId);
        order.setSeckillActivityId(activity.getId());
        /**
         * 创建 snowFlakeId
         */
        order.setOrderNo(String.valueOf(snowFlake.nextId()));
        order.setOrderAmount(activity.getSeckillPrice().longValue());
        /**
         * 发送订单信息给mq
         */
        rocketMQService.sendMessage("seckill_order", JSON.toJSONString(order));

        return order;
    }

    public boolean stockDeductValidator(Long activityId) {
        String key = "stock:" + activityId;
        return redisService.stockDeductValidator(key);
    }
}
