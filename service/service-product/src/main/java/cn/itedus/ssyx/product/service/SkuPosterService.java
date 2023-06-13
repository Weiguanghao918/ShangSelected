package cn.itedus.ssyx.product.service;

import cn.itedus.ssyx.model.product.SkuPoster;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-13 14:53
 * @description: 商品Sku海报服务接口
 */
public interface SkuPosterService extends IService<SkuPoster> {
    /**
     * 根据SKu id获取海报列表
     * @param id Sku id
     * @return 海报列表
     */
    List<SkuPoster> findBySkuId(Long id);
}
