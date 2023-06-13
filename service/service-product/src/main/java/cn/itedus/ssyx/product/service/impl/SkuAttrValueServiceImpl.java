package cn.itedus.ssyx.product.service.impl;

import cn.itedus.ssyx.model.product.SkuAttrValue;
import cn.itedus.ssyx.product.mapper.SkuAttrValueMapper;
import cn.itedus.ssyx.product.service.SkuAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-13 15:00
 * @description: 商品Sku属性服务接口实现类
 */
@Service
public class SkuAttrValueServiceImpl extends ServiceImpl<SkuAttrValueMapper, SkuAttrValue> implements SkuAttrValueService {
    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Override
    public List<SkuAttrValue> findBySkuId(Long id) {
        List<SkuAttrValue> skuAttrValueList = skuAttrValueMapper.selectList(new LambdaQueryWrapper<SkuAttrValue>().eq(SkuAttrValue::getSkuId, id));
        return skuAttrValueList;
    }
}
