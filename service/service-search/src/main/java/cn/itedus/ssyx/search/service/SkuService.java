package cn.itedus.ssyx.search.service;


/**
 * @author: Guanghao Wei
 * @date: 2023-06-14 16:49
 * @description: 商品搜索列表服务类
 */

public interface SkuService {
    /**
     * 上架商品列表
     *
     * @param skuId Sku id
     */
    void upperSku(Long skuId);

    /**
     * 下架商品列表
     *
     * @param skuId Sku id
     */
    void lowerSku(Long skuId);
}
