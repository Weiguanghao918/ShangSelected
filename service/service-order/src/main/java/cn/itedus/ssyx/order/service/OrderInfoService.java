package cn.itedus.ssyx.order.service;

import cn.itedus.ssyx.model.order.OrderInfo;
import cn.itedus.ssyx.vo.order.OrderConfirmVo;
import cn.itedus.ssyx.vo.order.OrderSubmitVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author: Guanghao Wei
 * @date: 2023-07-03 15:20
 * @description: 订单服务接口
 */
public interface OrderInfoService extends IService<OrderInfo> {
    /**
     * 订单确认接口
     *
     * @return 订单确认信息
     */
    OrderConfirmVo confirmOrder();

    /**
     * 生成订单接口
     *
     * @param orderSubmitVo 订单提交对象
     * @return 订单号
     */
    Long submitOrder(OrderSubmitVo orderSubmitVo);

    /**
     * 根据订单Id获取订单详情
     *
     * @param orderId 订单Id
     * @return 订单详情项
     */
    OrderInfo getOrderInfoById(Long orderId);
}
