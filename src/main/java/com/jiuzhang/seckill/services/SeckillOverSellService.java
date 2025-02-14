package com.jiuzhang.seckill.services;

import com.jiuzhang.seckill.db.dao.SeckillActivityDao;
import com.jiuzhang.seckill.db.po.SeckillActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeckillOverSellService {
    @Autowired
    private SeckillActivityDao seckillActivityDao;

    //简单处理库存扣减
    public String processSeckill(long activityId) {
           SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(activityId);
           long availableStock = seckillActivity.getAvailableStock();
           String result;
           if (availableStock>0){
               // 抢购成功并更新库存，这样做有什么问题？如果同时两个请求进入了，但商品只有1个，就会发生OverSell情况
               result = "恭喜，抢购成功~";
               System.out.println(result);
               availableStock -= 1;
               seckillActivity.setAvailableStock(new Integer("" + availableStock));//为什么要""+Long ?
               seckillActivityDao.updateSeckillActivity(seckillActivity);
           }else {
               result = "来晚了，商品被抢购完了";
               System.out.println(result);
           }
           return result;
    }
}
