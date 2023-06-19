package cn.itedus.ssyx.search.service;


import cn.itedus.ssyx.model.search.SkuEs;

import java.util.List;

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

    /**
     * 获取爆款商品
     *
     * @return 爆款商品列表
     */
    List<SkuEs> findHotSkuList();
}
