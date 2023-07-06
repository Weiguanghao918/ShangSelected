package cn.itedus.ssyx.product.service.impl;

import cn.itedus.ssyx.common.config.RedissonConfig;
import cn.itedus.ssyx.common.constant.RedisConst;
import cn.itedus.ssyx.common.exception.SsyxException;
import cn.itedus.ssyx.common.result.ResultCodeEnum;
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
import cn.itedus.ssyx.vo.product.SkuStockLockVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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

    @Autowired
    private RedissonClient redissonClient;

    @Resource
    private RedisTemplate redisTemplate;

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

    @Override
    public List<SkuInfo> findNewPersonSkuInfoList() {
        LambdaQueryWrapper<SkuInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SkuInfo::getIsNewPerson, 1);
        lambdaQueryWrapper.eq(SkuInfo::getPublishStatus, 1);
        lambdaQueryWrapper.orderByDesc(SkuInfo::getStock);//根据库存排序
        Page<SkuInfo> page = new Page<>(1, 3);
        IPage<SkuInfo> skuInfoPage = skuInfoMapper.selectPage(page, lambdaQueryWrapper);
        List<SkuInfo> skuInfoList = skuInfoPage.getRecords();
        return skuInfoList;
    }

    @Override
    public Boolean checkAndLock(List<SkuStockLockVo> skuStockLockVoList, String orderNo) {
        if (CollectionUtils.isEmpty(skuStockLockVoList)) {
            throw new SsyxException(ResultCodeEnum.DATA_ERROR);
        }
        //遍历所有商品，验证库存并且锁定库存，需要具备原子性，使用分布式锁完成
        skuStockLockVoList.forEach(skuStockLockVo -> {
            this.checkLock(skuStockLockVo);
        });

        //一旦有一个库存锁定失败，那么所有的锁定成功的商品都要解锁库存
        boolean flag = skuStockLockVoList.stream().anyMatch(skuStockLockVo -> !skuStockLockVo.getIsLock());
        if (flag) {
            //所有锁定商品需要进行解锁
            skuStockLockVoList.stream().filter(skuStockLockVo -> skuStockLockVo.getIsLock()).forEach(skuStockLockVo -> {
                skuInfoMapper.unlockStock(skuStockLockVo.getSkuId(), skuStockLockVo.getSkuNum());
            });
            //相应锁定状态
            return false;
        }

        redisTemplate.opsForValue().set(RedisConst.SROCK_INFO + orderNo, skuStockLockVoList);

        return true;

    }

    @Override
    public void minusStock(String orderNo) {
        List<SkuStockLockVo> skuStockLockVoList = (List<SkuStockLockVo>) this.redisTemplate.opsForValue().get(RedisConst.SROCK_INFO + orderNo);
        if (CollectionUtils.isEmpty(skuStockLockVoList)) {
            return;
        }
        skuStockLockVoList.forEach(skuStockLockVo -> {
            skuInfoMapper.minusStock(skuStockLockVo.getSkuId(), skuStockLockVo.getSkuNum());
        });

        this.redisTemplate.delete(RedisConst.SROCK_INFO + orderNo);
    }

    private void checkLock(SkuStockLockVo skuStockLockVo) {
        //这里使用Redisson公平锁来完成分布式锁锁定库存的操作
        RLock rLock = redissonClient.getFairLock(RedisConst.SKUKEY_PREFIX + skuStockLockVo.getSkuId());
        rLock.lock();

        try {
            //检验库存：查询，返回的是满足要求的库存列表
            SkuInfo skuInfo = skuInfoMapper.checkStock(skuStockLockVo.getSkuId(), skuStockLockVo.getSkuNum());
            //如果不存在满足要求的产品，那么验证库存失败
            if (null == skuInfo) {
                skuStockLockVo.setIsLock(false);
                return;
            }
            //锁定库存：更新操作
            Integer rows = skuInfoMapper.lockStock(skuStockLockVo.getSkuId(), skuStockLockVo.getSkuNum());
            if (rows == 1) {
                skuStockLockVo.setIsLock(true);
            }

        } finally {
            rLock.unlock();
        }
    }
}
