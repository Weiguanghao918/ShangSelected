package cn.itedus.ssyx;

import cn.itedus.ssyx.model.search.SkuEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

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
}
