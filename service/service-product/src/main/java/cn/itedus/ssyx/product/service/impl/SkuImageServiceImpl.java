package cn.itedus.ssyx.product.service.impl;

import cn.itedus.ssyx.model.product.SkuImage;
import cn.itedus.ssyx.product.mapper.SkuImageMapper;
import cn.itedus.ssyx.product.service.SkuImageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-13 14:58
 * @description: 商品Sku图像服务接口实现类
 */
@Service
public class SkuImageServiceImpl extends ServiceImpl<SkuImageMapper, SkuImage> implements SkuImageService {
    @Autowired
    private SkuImageMapper skuImageMapper;

    @Override
    public List<SkuImage> findBySkuId(Long id) {
        List<SkuImage> skuImageList = skuImageMapper.selectList(new LambdaQueryWrapper<SkuImage>().eq(SkuImage::getSkuId, id));
        return skuImageList;
    }
}
