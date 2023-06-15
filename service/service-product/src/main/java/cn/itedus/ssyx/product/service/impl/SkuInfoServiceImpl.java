package cn.itedus.ssyx.product.service.impl;

import cn.itedus.ssyx.model.product.SkuAttrValue;
import cn.itedus.ssyx.model.product.SkuImage;
import cn.itedus.ssyx.model.product.SkuInfo;
import cn.itedus.ssyx.model.product.SkuPoster;
import cn.itedus.ssyx.mq.constant.MqConst;
import cn.itedus.ssyx.mq.service.RabbitService;
import cn.itedus.ssyx.product.mapper.SkuInfoMapper;
import cn.itedus.ssyx.product.service.SkuAttrValueService;
import cn.itedus.ssyx.product.service.SkuImageService;
import cn.itedus.ssyx.product.service.SkuInfoService;
import cn.itedus.ssyx.product.service.SkuPosterService;
import cn.itedus.ssyx.vo.product.SkuInfoQueryVo;
import cn.itedus.ssyx.vo.product.SkuInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-13 14:43
 * @description: 商品SKU服务实现类
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {
    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private SkuPosterService skuPosterService;

    @Autowired
    private SkuImageService skuImageService;

    @Autowired
    private SkuAttrValueService skuAttrValueService;

    @Autowired
    private RabbitService rabbitService;

    @Override
    public IPage<SkuInfo> selectPageInfo(Page<SkuInfo> pageParam, SkuInfoQueryVo skuInfoQueryVo) {
        String skuType = skuInfoQueryVo.getSkuType();
        Long categoryId = skuInfoQueryVo.getCategoryId();
        String keyword = skuInfoQueryVo.getKeyword();
        LambdaQueryWrapper<SkuInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (!StringUtils.isEmpty(skuType)) {
            lambdaQueryWrapper.eq(SkuInfo::getSkuType, skuType);
        }
        if (!StringUtils.isEmpty(categoryId)) {
            lambdaQueryWrapper.eq(SkuInfo::getCategoryId, categoryId);
        }
        if (!StringUtils.isEmpty(keyword)) {
            lambdaQueryWrapper.like(SkuInfo::getSkuName, keyword);
        }

        IPage<SkuInfo> iPageModel = skuInfoMapper.selectPage(pageParam, lambdaQueryWrapper);
        return iPageModel;

    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void saveSkuInfo(SkuInfoVo skuInfoVo) {
        //1. 保存Sku信息
        SkuInfo skuInfo = new SkuInfoVo();
        BeanUtils.copyProperties(skuInfoVo, skuInfo);
        skuInfoMapper.insert(skuInfo);

        //2. 保存sku海报
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
        if (!CollectionUtils.isEmpty(skuPosterList)) {
            int sort = 1;
            for (SkuPoster skuPoster : skuPosterList) {
                skuPoster.setSkuId(skuInfo.getId());
                sort++;
            }
            skuPosterService.saveBatch(skuPosterList);
        }

        //3. 保存Sku图片
        List<SkuImage> skuImagesList = skuInfoVo.getSkuImagesList();
        if (!CollectionUtils.isEmpty(skuImagesList)) {
            int sort = 1;
            for (SkuImage skuImage : skuImagesList) {
                skuImage.setSkuId(skuInfo.getId());
                sort++;
            }
            skuImageService.saveBatch(skuImagesList);
        }

        //4. 保存Sku平台属性
        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
        if (!CollectionUtils.isEmpty(skuAttrValueList)) {
            int sort = 1;
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
                skuAttrValue.setSort(sort);
                sort++;
            }
            skuAttrValueService.saveBatch(skuAttrValueList);
        }
    }

    @Override
    public SkuInfoVo getSkuInfoVo(Long id) {
        SkuInfoVo skuInfoVo = new SkuInfoVo();
        SkuInfo skuInfo = skuInfoMapper.selectById(id);
        List<SkuImage> skuImageList = skuImageService.findBySkuId(id);
        List<SkuPoster> skuPosterList = skuPosterService.findBySkuId(id);
        List<SkuAttrValue> skuAttrValueList = skuAttrValueService.findBySkuId(id);

        BeanUtils.copyProperties(skuInfo, skuInfoVo);
        skuInfoVo.setSkuImagesList(skuImageList);
        skuInfoVo.setSkuPosterList(skuPosterList);
        skuInfoVo.setSkuAttrValueList(skuAttrValueList);
        return skuInfoVo;
    }

    @Override
    public void updateSkuInfoById(SkuInfoVo skuInfoVo) {
        Long id = skuInfoVo.getId();
        //更新SkuInfo信息
        skuInfoMapper.updateById(skuInfoVo);

        //2. 修改海报信息，先删除在重新写入
        skuPosterService.remove(new LambdaQueryWrapper<SkuPoster>().eq(SkuPoster::getSkuId, id));
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
        if (!CollectionUtils.isEmpty(skuPosterList)) {
            int sort = 1;
            for (SkuPoster skuPoster : skuPosterList) {
                skuPoster.setSkuId(id);
                sort++;
            }
            skuPosterService.saveBatch(skuPosterList);
        }

        //3. 修改图片信息，先删除在重新写入
        skuImageService.remove(new LambdaQueryWrapper<SkuImage>().eq(SkuImage::getSkuId, id));
        List<SkuImage> skuImagesList = skuInfoVo.getSkuImagesList();
        if (!CollectionUtils.isEmpty(skuImagesList)) {
            int sort = 1;
            for (SkuImage skuImage : skuImagesList) {
                skuImage.setSkuId(id);
                sort++;
            }
            skuImageService.saveBatch(skuImagesList);
        }

        //4. 修改平台属性信息，先删除在重新写入
        skuAttrValueService.remove(new LambdaQueryWrapper<SkuAttrValue>().eq(SkuAttrValue::getSkuId, id));
        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
        if (!CollectionUtils.isEmpty(skuAttrValueList)) {
            int sort = 1;
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(id);
                skuAttrValue.setSort(sort);
                sort++;
            }
            skuAttrValueService.saveBatch(skuAttrValueList);
        }
    }

    @Override
    public void checkStatus(Long skuId, Integer status) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setCheckStatus(status);
        skuInfoMapper.updateById(skuInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void publishSku(Long skuId, Integer status) {
        if (status == 1) {
            SkuInfo skuInfo = new SkuInfo();
            skuInfo.setId(skuId);
            skuInfo.setPublishStatus(1);
            skuInfoMapper.updateById(skuInfo);
            //商品上架：发送mq消息同步ES
            rabbitService.sendMessage(MqConst.EXCHANGE_GOODS_DIRECT, MqConst.ROUTING_GOODS_UPPER, skuId);
        } else {
            SkuInfo skuInfo = new SkuInfo();
            skuInfo.setId(skuId);
            skuInfo.setPublishStatus(0);
            skuInfoMapper.updateById(skuInfo);
            //商品下架：发送mq消息同步ES
            rabbitService.sendMessage(MqConst.EXCHANGE_GOODS_DIRECT, MqConst.ROUTING_GOODS_LOWER, skuId);
        }
    }

    @Override
    public void isNerPerson(Long skuId, Integer status) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsNewPerson(status);
        skuInfoMapper.updateById(skuInfo);
    }

    @Override
    public void removeBySKuId(Long skuId) {
        //首先删除SkuInfo表中的数据
        skuInfoMapper.deleteById(skuId);
        //删除图片、海报和属性
        skuImageService.remove(new LambdaQueryWrapper<SkuImage>().eq(SkuImage::getSkuId, skuId));
        skuPosterService.remove(new LambdaQueryWrapper<SkuPoster>().eq(SkuPoster::getSkuId, skuId));
        skuAttrValueService.remove(new LambdaQueryWrapper<SkuAttrValue>().eq(SkuAttrValue::getSkuId, skuId));
        //发送MQ消息，同步删除ES中的内容，其实就是和下架一样的
        rabbitService.sendMessage(MqConst.EXCHANGE_GOODS_DIRECT, MqConst.ROUTING_GOODS_LOWER, skuId);
    }

    @Override
    public void batchRemoveBySKuIds(List<Long> idList) {
        for (long skuId : idList) {
            //首先删除SkuInfo表中的数据
            skuInfoMapper.deleteById(skuId);
            //删除图片、海报和属性
            skuImageService.remove(new LambdaQueryWrapper<SkuImage>().eq(SkuImage::getSkuId, skuId));
            skuPosterService.remove(new LambdaQueryWrapper<SkuPoster>().eq(SkuPoster::getSkuId, skuId));
            skuAttrValueService.remove(new LambdaQueryWrapper<SkuAttrValue>().eq(SkuAttrValue::getSkuId, skuId));
            //发送MQ消息，同步删除ES中的内容，其实就是和下架一样的
            rabbitService.sendMessage(MqConst.EXCHANGE_GOODS_DIRECT, MqConst.ROUTING_GOODS_LOWER, skuId);
        }
    }

    @Override
    public List<SkuInfo> findSkuInfoList(List<Long> idList) {
        List<SkuInfo> skuInfoList = skuInfoMapper.selectBatchIds(idList);
        return skuInfoList;
    }

    @Override
    public List<SkuInfo> findSkuInfoListByKeyword(String keyword) {
        LambdaQueryWrapper<SkuInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(SkuInfo::getSkuName, keyword);
        List<SkuInfo> skuInfoList = skuInfoMapper.selectList(lambdaQueryWrapper);
        return skuInfoList;
    }
}
