package cn.itedus.ssyx.activity.service;

import cn.itedus.ssyx.model.activity.CouponInfo;
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
}
