package cn.itedus.ssyx.activity.service;

import cn.itedus.ssyx.model.activity.ActivityInfo;
import cn.itedus.ssyx.model.activity.ActivityRule;
import cn.itedus.ssyx.model.activity.CouponInfo;
import cn.itedus.ssyx.model.order.CartInfo;
import cn.itedus.ssyx.model.product.SkuInfo;
import cn.itedus.ssyx.vo.activity.ActivityRuleVo;
import cn.itedus.ssyx.vo.order.CartInfoVo;
import cn.itedus.ssyx.vo.order.OrderConfirmVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-15 14:49
 * @description: 营销活动服务接口
 */
public interface ActivityInfoService extends IService<ActivityInfo> {
    /**
     * 获取活动分页列表
     *
     * @param pageParam 分页数据
     * @return 分页列表
     */
    IPage<ActivityInfo> selectPage(Page<ActivityInfo> pageParam);

    /**
     * 根据活动Id 获取活动规则,其实就是要封装activityRule和SKu商品信息
     *
     * @param activityId 活动Id
     * @return 规则map
     */
    Map<String, Object> findActivityRuleList(Long activityId);

    /**
     * 插入活动规则信息
     *
     * @param activityRuleVo 活动规则vo类
     */
    void saveActivityRule(ActivityRuleVo activityRuleVo);

    /**
     * 根据关键字获取Sku列表，就是增加需要参加活动的商品Sku信息，只是需要把已经参加活动的SKu排除，返回剩余没有参与活动的SKu列表即可
     *
     * @param keyword
     * @return
     */
    List<SkuInfo> findSkuInfoByKeyword(String keyword);

    /**
     * 根据SKu id获取促销规则信息
     *
     * @param skuId Sku id
     * @return 促销规则集合
     */
    List<ActivityRule> findActivityRule(Long skuId);

    /**
     * 根据sku Id列表获取对应的规则信息
     *
     * @param skuIdList sku商品Id列表
     * @return 封装后的map集合，其中，SkuId为键，促销活动描述为值
     */
    Map<Long, List<String>> findActivity(List<Long> skuIdList);

    /**
     * 根据 skuId和userId 封装活动和促销券信息
     *
     * @param skuId  skuId
     * @param userId userId
     * @return 封装集合
     */
    Map<String, Object> findActivityAndCoupon(Long skuId, Long userId);

    /**
     * 获取购物车满足条件的促销与优惠券信息
     *
     * @param cartInfoList 购物车项集合
     * @param userId       userId
     * @return 订单vo类
     */
    OrderConfirmVo findCartActivityAndCoupon(List<CartInfo> cartInfoList, Long userId);

    /**
     * 封装cartInfoVo集合
     *
     * @param cartInfoList 购物车项集合
     * @return 封装结果
     */
    List<CartInfoVo> findCartActivityList(List<CartInfo> cartInfoList);


}
