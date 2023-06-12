package cn.itedus.ssyx.sys.service.impl;

import cn.itedus.ssyx.common.exception.SsyxException;
import cn.itedus.ssyx.common.result.ResultCodeEnum;
import cn.itedus.ssyx.sys.mapper.RegionWareMapper;
import cn.itedus.ssyx.model.sys.RegionWare;
import cn.itedus.ssyx.sys.service.RegionWareService;
import cn.itedus.ssyx.vo.sys.RegionWareQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-09 15:51
 * @description: 仓库-区域服务实现类
 */
@Service
public class RegionWareServiceImpl extends ServiceImpl<RegionWareMapper, RegionWare> implements RegionWareService {
    @Autowired
    private RegionWareMapper regionWareMapper;

    @Override
    public IPage<RegionWare> selectPageInfo(Page<RegionWare> pageParam, RegionWareQueryVo regionWareQueryVo) {
        LambdaQueryWrapper<RegionWare> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(regionWareQueryVo.getKeyword())) {
            lambdaQueryWrapper.like(RegionWare::getRegionName, regionWareQueryVo.getKeyword())
                    .or().like(RegionWare::getWareName, regionWareQueryVo.getKeyword());
        }
        IPage<RegionWare> iPageResult = regionWareMapper.selectPage(pageParam, lambdaQueryWrapper);
        return iPageResult;
    }

    @Override
    public void saveRegionWare(RegionWare regionWare) {
        LambdaQueryWrapper<RegionWare> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RegionWare::getRegionId, regionWare.getRegionId());
        regionWareMapper.selectCount(lambdaQueryWrapper);
        if (count() > 0) {
            throw new SsyxException(ResultCodeEnum.REGION_OPEN);
        }
        regionWareMapper.insert(regionWare);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        RegionWare regionWare = regionWareMapper.selectById(id);
        regionWare.setStatus(status);
        regionWareMapper.updateById(regionWare);
    }
}
