package cn.itedus.ssyx.activity.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-20 15:59
 * @description: 活动对外远程调用接口
 */
@FeignClient("service-activity")
public interface ActivityFeignClient {
    /**
     * 根据skuId集合封装对应的规则信息后存入map
     *
     * @param skuIdList skuId集合
     * @return 封装后的数据，其中skuId为键，activityRule规则为值
     */
    @PostMapping("/api/activity/inner/findActivity")
    public Map<Long, List<String>> findActivity(@RequestBody List<Long> skuIdList);

    /**
     * 根据skuId和userId获取活动与优惠券信息
     *
     * @param skuId  skuId
     * @param userId userId
     * @return 结果map
     */
    @GetMapping("/api/activity/inner/findActivityAndCoupon/{skuId}/{userId}")
    public Map<String, Object> findActivityAndCoupon(@PathVariable("skuId") Long skuId,
                                                     @PathVariable("userId") Long userId);

}
