package cn.itedus.ssyx.product.service.impl;


import cn.itedus.ssyx.model.product.SkuPoster;
import cn.itedus.ssyx.product.mapper.SkuPosterMapper;
import cn.itedus.ssyx.product.service.SkuPosterService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-13 14:55
 * @description: 商品Sku海报服务实现类
 */
@Service
public class SkuPosterServiceImpl extends ServiceImpl<SkuPosterMapper, SkuPoster> implements SkuPosterService {
    @Autowired
    private SkuPosterMapper skuPosterMapper;

    @Override
    public List<SkuPoster> findBySkuId(Long id) {
        List<SkuPoster> skuPosterList = skuPosterMapper.selectList(new LambdaQueryWrapper<SkuPoster>().eq(SkuPoster::getSkuId, id));
        return skuPosterList;
    }
}
