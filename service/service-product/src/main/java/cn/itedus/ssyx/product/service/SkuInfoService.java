package cn.itedus.ssyx.product.service;

import cn.itedus.ssyx.model.product.SkuInfo;
import cn.itedus.ssyx.vo.product.SkuInfoQueryVo;
import cn.itedus.ssyx.vo.product.SkuInfoVo;
import cn.itedus.ssyx.vo.product.SkuStockLockVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
     *
     * @param skuInfoVo 商品Skuvo类
     */
    void updateSkuInfoById(SkuInfoVo skuInfoVo);

    /**
     * 商品Sku状态审核
     *
     * @param skuId  Sku id
     * @param status 当前状态码
     */
    void checkStatus(Long skuId, Integer status);

    /**
     * 商品上架功能
     *
     * @param skuId  商品Sku id
     * @param status 当前上架状态
     */
    void publishSku(Long skuId, Integer status);

    /**
     * 修改是否新人专享状态
     *
     * @param skuId  商品SKu id
     * @param status 新人专享状态
     */
    void isNerPerson(Long skuId, Integer status);

    /**
     * 删除商品Sku消息
     *
     * @param skuId SKu id
     */
    void removeBySKuId(Long skuId);

    /**
     * 批量删除Sku商品
     *
     * @param idList Sku商品Id集合
     */
    void batchRemoveBySKuIds(List<Long> idList);

    /**
     * 根据id列表获取Sku集合
     *
     * @param idList SKu id集合
     * @return Sku集合
     */
    List<SkuInfo> findSkuInfoList(List<Long> idList);

    /**
     * 根据关键字获取Sku集合
     *
     * @param keyword 关键字
     * @return Sku集合
     */
    List<SkuInfo> findSkuInfoListByKeyword(String keyword);

    /**
     * 获取新人专享商品
     *
     * @return 新人专享Sku列表
     */
    List<SkuInfo> findNewPersonSkuInfoList();

    /**
     * 锁定商品库存
     *
     * @param skuStockLockVoList 商品列表
     * @param orderNo            订单号
     * @return 是否成功
     */
    Boolean checkAndLock(List<SkuStockLockVo> skuStockLockVoList, String orderNo);

    /**
     * 支付成功后，扣减库存操作
     *
     * @param orderNo 订单编号
     */
    void minusStock(String orderNo);
}
