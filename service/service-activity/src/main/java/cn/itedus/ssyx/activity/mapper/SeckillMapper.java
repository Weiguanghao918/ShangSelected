package cn.itedus.ssyx.activity.mapper;

import cn.itedus.ssyx.model.activity.Seckill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-15 19:33
 * @description: 秒杀活动仓储类
 */
@Mapper
public interface SeckillMapper extends BaseMapper<Seckill> {
}
