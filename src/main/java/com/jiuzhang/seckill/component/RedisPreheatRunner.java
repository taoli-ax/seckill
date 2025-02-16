package com.jiuzhang.seckill.component;

import com.jiuzhang.seckill.db.dao.SeckillActivityDao;
import com.jiuzhang.seckill.db.po.SeckillActivity;
import com.jiuzhang.seckill.util.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisPreheatRunner implements ApplicationRunner {
    @Autowired
    private RedisService redisService;

    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<SeckillActivity> activities = seckillActivityDao.querySeckillActivitysByStatus(1);
        for (SeckillActivity activity : activities) {
            redisService.setValue("stock:"+activity.getId(),(long)activity.getAvailableStock());
        }
    }
}
