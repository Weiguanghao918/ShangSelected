package cn.itedus.ssyx.product.service;

import cn.itedus.ssyx.model.product.SkuAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-13 15:00
 * @description: 商品Sku属性服务接口
 */
public interface SkuAttrValueService extends IService<SkuAttrValue> {
    /**
     * 根据SKu id获取平台属性信息
     * @param id Sku id
     * @return 平台属性列表
     */
    List<SkuAttrValue> findBySkuId(Long id);
}
