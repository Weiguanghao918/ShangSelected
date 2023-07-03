package cn.itedus.ssyx.order.mapper;

import cn.itedus.ssyx.model.order.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: Guanghao Wei
 * @date: 2023-07-03 15:20
 * @description: 订单服务仓储类
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
}
