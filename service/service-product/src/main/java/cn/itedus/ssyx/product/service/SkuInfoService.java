package cn.itedus.ssyx.product.service;

import cn.itedus.ssyx.model.product.SkuInfo;
import cn.itedus.ssyx.vo.product.SkuInfoQueryVo;
import cn.itedus.ssyx.vo.product.SkuInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-13 14:43
 * @description: 商品SKU服务接口
 */
public interface SkuInfoService extends IService<SkuInfo> {
    /**
     * 获取商品Sku分页列表
     *
     * @param pageParam      分页数据
     * @param skuInfoQueryVo 查询对象
     * @return 分页列表
     */
    IPage<SkuInfo> selectPageInfo(Page<SkuInfo> pageParam, SkuInfoQueryVo skuInfoQueryVo);

    /**
     * 新增商品Sku信息
     *
     * @param skuInfoVo 入参对象
     */
    void saveSkuInfo(SkuInfoVo skuInfoVo);

    /**
     * 获取Sku完整信息、包括info、poster、image、attr
     *
     * @param id SkuID
     * @return Sku完整信息
     */
    SkuInfoVo getSkuInfoVo(Long id);

    /**
     * 修改商品Sku信息
     * @param skuInfoVo 商品Skuvo类
     */
    void updateSkuInfoById(SkuInfoVo skuInfoVo);

    /**
     * 商品Sku状态审核
     * @param skuId Sku id
     * @param status 当前状态码
     */
    void checkStatus(Long skuId, Integer status);

    /**
     * 商品上架功能
     * @param skuId 商品Sku id
     * @param status 当前上架状态
     */
    void publishSku(Long skuId, Integer status);

    /**
     * 修改是否新人专享状态
     * @param skuId 商品SKu id
     * @param status 新人专享状态
     */
    void isNerPerson(Long skuId, Integer status);
}
