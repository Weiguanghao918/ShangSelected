package cn.itedus.ssyx.home.service;

import java.util.Map;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-25 16:09
 * @description: 小程序端商品详情服务接口
 */
public interface ItemService {
    /**
     * 获取商品Sku详情信息
     *
     * @param skuId  商品SkuId
     * @param userId 用户id
     * @return Sku详情信息
     */
    Map<String, Object> getSkuDetailIndex(Long skuId, Long userId);
}
