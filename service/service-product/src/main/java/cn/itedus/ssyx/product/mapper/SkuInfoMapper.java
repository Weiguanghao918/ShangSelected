package cn.itedus.ssyx.product.mapper;

import cn.itedus.ssyx.model.product.SkuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-13 14:42
 * @description: 商品SKU仓储类
 */
@Mapper
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {
    /**
     * 解锁库存
     *
     * @param skuId  skuId
     * @param skuNum skuNum
     * @return 影响行数
     */
    Integer unlockStock(@Param("skuId") Long skuId, @Param("skuNum") Integer skuNum);

    /**
     * 检验库存
     *
     * @param skuId  skuID
     * @param skuNum skuNum
     * @return sku商品信息
     */
    SkuInfo checkStock(@Param("skuId") Long skuId, @Param("skuNum") Integer skuNum);

    /**
     * 锁定库存
     *
     * @param skuId  skuId
     * @param skuNum skuNum
     * @return 影响行数
     */
    Integer lockStock(@Param("skuId") Long skuId, @Param("skuNum") Integer skuNum);

    /**
     * 扣减库存
     *
     * @param skuId  skuId
     * @param skuNum SKu数量
     */
    void minusStock(@Param("skuId") Long skuId, @Param("skuNum") Integer skuNum);
}
