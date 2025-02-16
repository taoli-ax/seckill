package com.jiuzhang.seckill.db.dao;

import com.jiuzhang.seckill.db.mappers.SeckillOrderMapper;
import com.jiuzhang.seckill.db.po.SeckillOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Slf4j
@Repository
public class SeckillOrderDaoImpl implements SeckillOrderDao {

    @Resource
    private SeckillOrderMapper seckillOrderMapper;

    @Override
    public void insertOrder(SeckillOrder order) {
        seckillOrderMapper.insert(order);
    }

    @Override
    public void updateOrder(SeckillOrder order) {
        seckillOrderMapper.updateByPrimaryKey(order);
    }

    @Override
    public SeckillOrder queryOrder(String orderNo) {
       return seckillOrderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public boolean lockStock(long seckillActivityId) {
       int result =  seckillOrderMapper.lockStock(seckillActivityId);
       if (result < 1) {
           log.info("锁定库存失败");
           return false;
       }
       return true;
    }
}
