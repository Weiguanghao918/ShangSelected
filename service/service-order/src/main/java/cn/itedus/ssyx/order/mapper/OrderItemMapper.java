package cn.itedus.ssyx.order.mapper;

import cn.itedus.ssyx.model.order.OrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: Guanghao Wei
 * @date: 2023-07-04 16:30
 * @description: 订单项仓储类
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
