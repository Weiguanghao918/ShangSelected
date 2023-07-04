package cn.itedus.ssyx.activity.service;

import cn.itedus.ssyx.model.activity.CouponInfo;
import cn.itedus.ssyx.model.activity.CouponRange;
import cn.itedus.ssyx.model.order.CartInfo;
import cn.itedus.ssyx.vo.activity.CouponRuleVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-15 14:52
 * @description: 优惠券服务接口
 */
public interface CouponInfoService extends IService<CouponInfo> {
    /**
     * 获取优惠券分页列表
     *
     * @param pageParam 分页信息
     * @return 分页列表
     */
    IPage<CouponInfo> selectPage(Page<CouponInfo> pageParam);

    /**
     * 根据Id获取优惠券
     *
     * @param id 优惠券Id
     * @return 优惠券信息
     */
    CouponInfo getCouponInfo(Long id);

    /**
     * 根据优惠券Id获取优惠券规则列表
     *
     * @param id 优惠券Id
     * @return 规则列表
     */
    Map<String, Object> findCouponRuleList(Long id);

    /**
     * 新增优惠券规则
     *
     * @param couponRuleVo
     */
    void saveCouponRule(CouponRuleVo couponRuleVo);

    /**
     * 根据关键字获取优惠券列表，活动使用
     *
     * @param keyword 关键字
     * @return 优惠券列表
     */
    List<CouponInfo> findCouponByKeyword(String keyword);

    /**
     * 根据skuId和userId 获取优惠券信息
     *
     * @param skuId  skuId
     * @param userId userId
     * @return 优惠券信息
     */
    List<CouponInfo> findCouponInfo(Long skuId, Long userId);

    /**
     * 查找优惠券信息
     *
     * @param cartInfoList 购物车项集合
     * @param userId       userId
     * @return 购物券集合
     */
    List<CouponInfo> findCartCouponInfo(List<CartInfo> cartInfoList, Long userId);

    /**
     * 获取优惠券id对应的满足使用范围的购物项skuId列表
     * 说明：一个优惠券可能有多个购物项满足它的使用范围，那么多个购物项可以拼单使用这个优惠券
     *
     * @param cartInfoList
     * @param couponRangeList
     * @return
     */
    Map<Long, List<Long>> findCouponIdToSkuIdMap(List<CartInfo> cartInfoList, List<CouponRange> couponRangeList);


    /**
     * 封装优惠券信息，将优惠券设计的商品ID进行封装
     *
     * @param cartInfoList 商品集合
     * @param couponId     优惠券Id
     * @return 封装类
     */
    CouponInfo findRangeSkuIdList(List<CartInfo> cartInfoList, Long couponId);

    /**
     * 更新优惠券使用状态
     *
     * @param couponId 优惠券ID
     * @param userId   用户ID
     * @param orderId  订单ID
     */
    void updateCouponInfoUseStatus(Long couponId, Long userId, Long orderId);
}
