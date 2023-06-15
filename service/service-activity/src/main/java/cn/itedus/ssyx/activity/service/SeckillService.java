package cn.itedus.ssyx.activity.service;

import cn.itedus.ssyx.model.activity.Seckill;
import cn.itedus.ssyx.vo.activity.SeckillQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-15 19:32
 * @description: 秒杀活动服务接口
 */
public interface SeckillService extends IService<Seckill> {
    /**
     * 获取秒杀活动分页列表
     * @param pageParam 分页数据
     * @param seckillQueryVo 查询条件
     * @return 分页列表
     */
    IPage<Seckill> selectPage(Page<Seckill> pageParam, SeckillQueryVo seckillQueryVo);

    /**
     * 修改秒杀活动状态
     * @param id 活动Id
     * @param status 修改状态
     */
    void updateSeckillStatus(Long id, Integer status);
}
