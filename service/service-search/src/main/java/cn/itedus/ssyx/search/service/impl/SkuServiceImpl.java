package cn.itedus.ssyx.search.service.impl;

import cn.itedus.ssyx.client.product.ProductFeignClient;
import cn.itedus.ssyx.enums.SkuType;
import cn.itedus.ssyx.model.product.Category;
import cn.itedus.ssyx.model.product.SkuInfo;
import cn.itedus.ssyx.model.search.SkuEs;
import cn.itedus.ssyx.search.repository.SkuRepository;
import cn.itedus.ssyx.search.service.SkuService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-14 16:49
 * @description: 商品搜索服务实现类
 */
@Service
@Slf4j
public class SkuServiceImpl implements SkuService {
    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private SkuRepository skuRepository;
    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @Override
    public void upperSku(Long skuId) {
        log.info("upperSku:" + skuId);
        SkuEs skuEs = new SkuEs();

        //查询Sku信息
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        if (null == skuInfo) {
            return;
        }
        //查询分类
        Category category = productFeignClient.getCategory(skuInfo.getCategoryId());
        if (category != null) {
            skuEs.setCategoryId(category.getId());
            skuEs.setCategoryName(category.getName());
        }

        skuEs.setId(skuInfo.getId());
        skuEs.setKeyword(skuInfo.getSkuName() + "," + skuEs.getCategoryName());
        skuEs.setWareId(skuInfo.getWareId());
        skuEs.setIsNewPerson(skuInfo.getIsNewPerson());
        skuEs.setImgUrl(skuInfo.getImgUrl());
        skuEs.setTitle(skuInfo.getSkuName());
        if (skuInfo.getSkuType().equals(SkuType.COMMON.getCode())) {
            skuEs.setSkuType(0);
            skuEs.setPrice(skuInfo.getPrice().doubleValue());
            skuEs.setSale(skuInfo.getSale());
            skuEs.setPerLimit(skuInfo.getPerLimit());
        } else {
            //TODO 待完善-秒杀商品
        }
        SkuEs save = skuRepository.save(skuEs);
        log.info("upperSku:" + JSON.toJSONString(save));
    }

    @Override
    public void lowerSku(Long skuId) {
        this.skuRepository.deleteById(skuId);
    }
}
