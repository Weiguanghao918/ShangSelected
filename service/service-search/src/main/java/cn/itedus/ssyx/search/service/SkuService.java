package cn.itedus.ssyx.search.service;


import cn.itedus.ssyx.model.search.SkuEs;
import cn.itedus.ssyx.vo.search.SkuEsQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    /**
     * 搜索列表
     *
     * @param pageable     分页信息
     * @param skuEsQueryVo 查询条件
     * @return 商品分裂信息
     */
    Page<SkuEs> search(Pageable pageable, SkuEsQueryVo skuEsQueryVo);

    /**
     * 增加商品热度
     * @param skuId skuId
     */
    void incrHtScore(Long skuId);
}
