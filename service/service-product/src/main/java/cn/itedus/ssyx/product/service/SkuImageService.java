package cn.itedus.ssyx.product.service;

import cn.itedus.ssyx.model.product.SkuImage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-13 14:57
 * @description: 商品Sku图像服务接口
 */
public interface SkuImageService extends IService<SkuImage> {
    /**
     * 根据Skuid查询图片信息
     *
     * @param id Skuid
     * @return 图片列表
     */
    List<SkuImage> findBySkuId(Long id);
}
