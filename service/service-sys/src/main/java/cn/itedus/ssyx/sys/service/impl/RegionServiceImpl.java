package cn.itedus.ssyx.sys.service.impl;

import cn.itedus.ssyx.sys.mapper.RegionMapper;
import cn.itedus.ssyx.model.sys.Region;
import cn.itedus.ssyx.sys.service.RegionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-09 15:52
 * @description: 区域服务接口类
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {
    @Autowired
    private RegionMapper regionMapper;

    @Override
    public List<Region> findRegionByKeyword(String keyWord) {
        LambdaQueryWrapper<Region> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Region::getName, keyWord);
        List<Region> regionList = regionMapper.selectList(lambdaQueryWrapper);
        return regionList;
    }
}
