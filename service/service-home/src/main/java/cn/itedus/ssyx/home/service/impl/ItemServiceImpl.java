package cn.itedus.ssyx.home.service.impl;

import cn.itedus.ssyx.SkuFeignClient;
import cn.itedus.ssyx.activity.client.ActivityFeignClient;
import cn.itedus.ssyx.client.product.ProductFeignClient;
import cn.itedus.ssyx.home.service.ItemService;
import cn.itedus.ssyx.model.product.SkuInfo;
import cn.itedus.ssyx.vo.product.SkuInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-25 16:10
 * @description: 小程序端商品服务详情服务接口实现类
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ProductFeignClient productFeignClient;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private ActivityFeignClient activityFeignClient;

    @Autowired
    private SkuFeignClient skuFeignClient;

    @Override
    public Map<String, Object> getSkuDetailIndex(Long skuId, Long userId) {
        Map<String, Object> result = new HashMap<>();
        //1. 获取Sku基本信息
        CompletableFuture<Void> SkuCompletableFuture = CompletableFuture.runAsync(() -> {
            SkuInfoVo skuInfoVo = productFeignClient.getSkuInfoById(skuId);
            result.put("skuInfoVo", skuInfoVo);
        }, threadPoolExecutor);

        //2. 获取Sku有关的促销与优惠券信息
        CompletableFuture<Void> activityCompletableFuture = CompletableFuture.runAsync(() -> {
            Map<String, Object> activityAndCouponMap = activityFeignClient.findActivityAndCoupon(skuId, userId);
            result.putAll(activityAndCouponMap);
        }, threadPoolExecutor);

        //3. 增加商品热度
        CompletableFuture<Void> hotCompletableFuture = CompletableFuture.runAsync(() -> {
            skuFeignClient.incrHotScore(skuId);
        }, threadPoolExecutor);


        //组合任务
        CompletableFuture.allOf(SkuCompletableFuture, activityCompletableFuture, hotCompletableFuture).join();

        return result;
    }
}
