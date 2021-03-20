package com.qyl.mall.mapper;

import com.qyl.mall.pojo.SeckillTime;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author: qyl
 * @Date: 2020/12/7 15:02
 */
public interface SeckillTimeMapper extends Mapper<SeckillTime> {

    @Select("select * from seckill_time where end_time > #{time} limit 8")
    List<SeckillTime> getTime(long time);

    @Delete("delete from seckill_time")
    void deleteAll();

    @Select("select end_time from seckill_time where time_id = #{timeId}")
    Long getEndTime(Integer timeId);
}
