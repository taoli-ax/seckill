package com.jiuzhang.seckill;

import com.jiuzhang.seckill.util.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {
    @Resource
    private RedisService redisService;

    private final String key="abc:123";
    @Test
    public void socketTest(){
        redisService.setValue(key,10L);
    }

    @Test
    public void getSocketTest(){
        String value = redisService.getValue(key);
        System.out.println(value);
    }

    @Test
    public void stockDeductValidatorTest(){
        boolean hasStock = redisService.stockDeductValidator(key);
        System.out.println(hasStock);
        String value =redisService.getValue(key);
        System.out.println(value);
    }
}
