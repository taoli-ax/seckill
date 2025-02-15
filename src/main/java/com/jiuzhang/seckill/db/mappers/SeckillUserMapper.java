package com.jiuzhang.seckill.db.mappers;

import com.jiuzhang.seckill.db.po.SeckillUser;

public interface SeckillUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SeckillUser record);

    int insertSelective(SeckillUser record);

    SeckillUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillUser record);

    int updateByPrimaryKey(SeckillUser record);
}