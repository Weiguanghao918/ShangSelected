package cn.itedus.ssyx.payment.mapper;

import cn.itedus.ssyx.model.order.PaymentInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: Guanghao Wei
 * @date: 2023-07-06 13:47
 * @description: 下单详情服务仓储类
 */
@Mapper
public interface PaymentInfoMapper extends BaseMapper<PaymentInfo> {
}
