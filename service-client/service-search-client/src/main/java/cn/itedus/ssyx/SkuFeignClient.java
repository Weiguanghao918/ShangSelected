package cn.itedus.ssyx;

import cn.itedus.ssyx.model.search.SkuEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-19 20:04
 * @description:
 */
@FeignClient("service-search")
public interface SkuFeignClient {
    /**
     * 在es中获取热点数据
     *
     * @return 返回热点数据
     */
    @GetMapping("/api/search/sku/inner/findHotSkuList")
    public List<SkuEs> findHotSkuList();

    /**
     * 更新es中商品热度
     *
     * @param skuId skuId
     * @return 更新结果是否成功
     */
    @GetMapping("/api/search/sku/inner/incrHotScore/{skuId}")
    public Boolean incrHotScore(@PathVariable("skuId") Long skuId);
}
