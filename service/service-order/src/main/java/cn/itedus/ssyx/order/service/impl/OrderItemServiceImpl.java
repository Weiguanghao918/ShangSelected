package cn.itedus.ssyx.order.service.impl;

import cn.itedus.ssyx.model.order.OrderItem;
import cn.itedus.ssyx.order.mapper.OrderItemMapper;
import cn.itedus.ssyx.order.service.OrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author: Guanghao Wei
 * @date: 2023-07-04 16:30
 * @description: 订单项服务接口实现类
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {
}
