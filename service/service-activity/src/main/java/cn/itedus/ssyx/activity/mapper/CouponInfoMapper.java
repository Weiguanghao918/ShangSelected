package cn.itedus.ssyx.activity.mapper;

import cn.itedus.ssyx.model.activity.CouponInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-15 14:52
 * @description:
 */
@Mapper
public interface CouponInfoMapper extends BaseMapper<CouponInfo> {
    /**
     * 根据skuId、categoryId、userId获取优惠券信息
     *
     * @param skuId      skuId
     * @param categoryId categoryId
     * @param userId     userId
     * @return 优惠券信息
     */
    List<CouponInfo> selectCouponInfoList(@Param("skuId") Long skuId, @Param("categoryId") Long categoryId, @Param("userId") Long userId);

    List<CouponInfo> selectCartCouponInfoList(@Param("userId") Long userId);
}
