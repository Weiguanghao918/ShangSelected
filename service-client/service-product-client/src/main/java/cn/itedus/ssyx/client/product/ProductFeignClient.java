package cn.itedus.ssyx.client.product;

import cn.itedus.ssyx.model.product.Category;
import cn.itedus.ssyx.model.product.SkuInfo;
import cn.itedus.ssyx.vo.product.SkuInfoVo;
import cn.itedus.ssyx.vo.product.SkuStockLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-14 16:43
 * @description: 产品远程调用接口
 */
@FeignClient(value = "service-product")
public interface ProductFeignClient {
    /**
     * 根据分类Id获取分类信息
     *
     * @param categoryId 分类Id
     * @return 分类信息
     */
    @GetMapping("/api/product/inneer/getCategory/{categoryId}")
    public Category getCategory(@PathVariable("categoryId") Long categoryId);

    /**
     * 根据Sku id获取Sku信息
     *
     * @param skuId Sku Id
     * @return Sku信息
     */
    @GetMapping("/api/product/inner/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable("skuId") Long skuId);

    /**
     * 根据SKu id列表获取SKu集合
     *
     * @param idList Sku id列表
     * @return Sku集合
     */
    @PostMapping("/api/product/inner/findSkuInfoList")
    public List<SkuInfo> findSkuInfoList(@RequestBody List<Long> idList);

    /**
     * 根据关键字获取Sku结合
     *
     * @param keyword 关键字
     * @return Sku集合
     */
    @PostMapping("/api/product/inner/findSkuInfoByKeyword/{keyword}")
    public List<SkuInfo> findSkuInfoListByKeyword(@PathVariable("keyword") String keyword);

    /**
     * 根据Id列表获取分类集合
     *
     * @param idList 分类Id列表
     * @return 分类集合
     */
    @PostMapping("/api/product/inner/findCategoryList")
    public List<Category> findCategoryList(@RequestBody List<Long> idList);

    /**
     * 获取全部分类
     *
     * @return 分类列表
     */
    @GetMapping("/api/product/inner/findAllCategoryList")
    public List<Category> findAllCategoryList();

    /**
     * 获取新人专享SKu列表
     *
     * @return 新人专享SKu商品列表
     */
    @GetMapping("/api/product/inner/findNewPersonSkuInfoList")
    public List<SkuInfo> findNewPersonSkuInfoList();

    /**
     * 获取商品SkuInfoVo封装结果
     *
     * @param skuId skuId
     * @return 封装结果
     */
    @GetMapping("/api/product/inner/{id}")
    public SkuInfoVo getSkuInfoById(@PathVariable("id") Long skuId);

    /**
     * 校验库存并且锁定商品库存
     *
     * @param skuStockLockVoList 商品列表
     * @param orderNo            订单号
     * @return 是否成功
     */
    @PostMapping("/api/product/inner/ckeckAndLock/{orderNo}")
    public Boolean checkAndLock(@RequestBody List<SkuStockLockVo> skuStockLockVoList, @PathVariable("orderNo") String orderNo);
}
