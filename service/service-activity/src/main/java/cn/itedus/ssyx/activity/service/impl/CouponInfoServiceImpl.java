package cn.itedus.ssyx.activity.service.impl;

import cn.itedus.ssyx.activity.mapper.CouponInfoMapper;
import cn.itedus.ssyx.activity.mapper.CouponRangeMapper;
import cn.itedus.ssyx.activity.mapper.CouponUserMapper;
import cn.itedus.ssyx.activity.service.CouponInfoService;
import cn.itedus.ssyx.client.product.ProductFeignClient;
import cn.itedus.ssyx.enums.CouponRangeType;
import cn.itedus.ssyx.model.activity.CouponInfo;
import cn.itedus.ssyx.model.activity.CouponRange;
import cn.itedus.ssyx.model.product.Category;
import cn.itedus.ssyx.model.product.SkuInfo;
import cn.itedus.ssyx.vo.activity.CouponRuleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-15 14:52
 * @description: 优惠券服务接口实现类
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {
    @Autowired
    private CouponInfoMapper couponInfoMapper;
    @Autowired
    private CouponRangeMapper couponRangeMapper;
    @Autowired
    private CouponUserMapper couponUserMapper;
    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public IPage<CouponInfo> selectPage(Page<CouponInfo> pageParam) {
        QueryWrapper<CouponInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        IPage<CouponInfo> iPageModel = couponInfoMapper.selectPage(pageParam, queryWrapper);
        iPageModel.getRecords().stream().forEach(item -> {
            item.setCouponTypeString(item.getCouponType().getComment());
            if (null != item.getRangeType()) {
                item.setRangeTypeString(item.getRangeType().getComment());
            }
        });
        return iPageModel;
    }

    @Override
    public CouponInfo getCouponInfo(Long id) {
        CouponInfo couponInfo = couponInfoMapper.selectById(id);
        couponInfo.setCouponTypeString(couponInfo.getCouponType().getComment());
        if (null != couponInfo.getRangeType()) {
            couponInfo.setRangeTypeString(couponInfo.getRangeType().getComment());
        }
        return couponInfo;
    }

    @Override
    public Map<String, Object> findCouponRuleList(Long id) {
        Map<String, Object> result = new HashMap<>();
        CouponInfo couponInfo = couponInfoMapper.selectById(id);
        LambdaQueryWrapper<CouponRange> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CouponRange::getCouponId, id);
        List<CouponRange> couponRangeList = couponRangeMapper.selectList(lambdaQueryWrapper);
        List<Long> rangeIdList = couponRangeList.stream().map(CouponRange::getRangeId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(rangeIdList)) {
            if (couponInfo.getRangeType() == CouponRangeType.SKU) {
                List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoList(rangeIdList);
                result.put("skuInfoList", skuInfoList);
            } else if (couponInfo.getRangeType() == CouponRangeType.CATEGORY) {
                List<Category> categoryList = productFeignClient.findCategoryList(rangeIdList);
                result.put("categoryList", categoryList);
            } else {
                //TODO 通用券
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveCouponRule(CouponRuleVo couponRuleVo) {
        //删除原有数据
        couponRangeMapper.delete(new LambdaQueryWrapper<CouponRange>().eq(CouponRange::getCouponId, couponRuleVo.getCouponId()));
        //更新数据，优惠券和范围表都需要更新
        CouponInfo couponInfo = couponInfoMapper.selectById(couponRuleVo.getCouponId());
        couponInfo.setAmount(couponRuleVo.getAmount());
        couponInfo.setConditionAmount(couponRuleVo.getConditionAmount());
        couponInfo.setRangeType(couponRuleVo.getRangeType());
        couponInfo.setRangeDesc(couponRuleVo.getRangeDesc());

        couponInfoMapper.updateById(couponInfo);

        //插入优惠券范围
        List<CouponRange> couponRangeList = couponRuleVo.getCouponRangeList();
        for (CouponRange couponRange : couponRangeList) {
            couponRange.setCouponId(couponRuleVo.getCouponId());
            couponRangeMapper.insert(couponRange);
        }

    }

    @Override
    public List<CouponInfo> findCouponByKeyword(String keyword) {
        LambdaQueryWrapper<CouponInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(CouponInfo::getCouponName, keyword);
        List<CouponInfo> couponInfoList = couponInfoMapper.selectList(lambdaQueryWrapper);
        return couponInfoList;
    }
}
