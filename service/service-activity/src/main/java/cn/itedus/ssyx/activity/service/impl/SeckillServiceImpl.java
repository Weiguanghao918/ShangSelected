package cn.itedus.ssyx.activity.service.impl;

import cn.itedus.ssyx.activity.mapper.SeckillMapper;
import cn.itedus.ssyx.activity.service.SeckillService;
import cn.itedus.ssyx.model.activity.Seckill;
import cn.itedus.ssyx.vo.activity.SeckillQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-15 19:33
 * @description: 秒秒杀活动服务接口实现类
 */
@Service
public class SeckillServiceImpl extends ServiceImpl<SeckillMapper, Seckill> implements SeckillService {

    @Autowired
    private SeckillMapper seckillMapper;

    @Override
    public IPage<Seckill> selectPage(Page<Seckill> pageParam, SeckillQueryVo seckillQueryVo) {
        String title = seckillQueryVo.getTitle();
        Integer status = seckillQueryVo.getStatus();
        LambdaQueryWrapper<Seckill> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(title)) {
            lambdaQueryWrapper.like(Seckill::getTitle, title);
        }
        if (!StringUtils.isEmpty(status)) {
            lambdaQueryWrapper.eq(Seckill::getStatus, status);
        }
        IPage<Seckill> iPageModel = seckillMapper.selectPage(pageParam, lambdaQueryWrapper);
        return iPageModel;
    }

    @Override
    public void updateSeckillStatus(Long id, Integer status) {

    }
}
