package com.jiuzhang.seckill.db.dao;

import com.jiuzhang.seckill.db.mappers.SeckillOrderMapper;
import com.jiuzhang.seckill.db.po.SeckillOrder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

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
}
