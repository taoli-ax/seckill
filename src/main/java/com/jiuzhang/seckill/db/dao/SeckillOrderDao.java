package com.jiuzhang.seckill.db.dao;

import com.jiuzhang.seckill.db.po.SeckillOrder;

public interface SeckillOrderDao {
    public void insertOrder(SeckillOrder order);
    public void updateOrder(SeckillOrder order);
    public SeckillOrder queryOrder(String orderNo);

    boolean lockStock(long seckillActivityId);
}
