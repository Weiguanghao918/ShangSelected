package cn.itedus.ssyx.home.service.impl;

import cn.itedus.ssyx.SkuFeignClient;
import cn.itedus.ssyx.client.product.ProductFeignClient;
import cn.itedus.ssyx.client.user.UserFeignClient;
import cn.itedus.ssyx.home.service.HomeService;
import cn.itedus.ssyx.model.product.Category;
import cn.itedus.ssyx.model.product.SkuInfo;
import cn.itedus.ssyx.model.search.SkuEs;
import cn.itedus.ssyx.vo.user.LeaderAddressVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-19 19:13
 * @description: 首页服务接口实现类
 */
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private SkuFeignClient skuFeignClient;


    @Override
    public Map<String, Object> homeData(Long userId) {
        Map<String, Object> result = new HashMap<>();
        //1. 获取分类信息
        List<Category> categoryList = productFeignClient.findAllCategoryList();
        result.put("categoryList", categoryList);

        //2.获取新人专享商品
        List<SkuInfo> newPersonSkuInfoList = productFeignClient.findNewPersonSkuInfoList();
        result.put("newPersonSkuInfoList", newPersonSkuInfoList);

        //3. TODO :获取用户首页秒杀数据

        //4.获取提货地址信息
        LeaderAddressVo leaderAddressVo = userFeignClient.getUserAddressByUserId(userId);
        result.put("leaderAddressVo", leaderAddressVo);

        //5.获取爆品商品
        List<SkuEs> hotSkuList = skuFeignClient.findHotSkuList();
        result.put("hotSkuList", hotSkuList);

        return result;
    }
}
