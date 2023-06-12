package cn.itedus.ssyx.sys.service;

import cn.itedus.ssyx.model.sys.RegionWare;
import cn.itedus.ssyx.vo.sys.RegionWareQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-09 15:51
 * @description: 仓库-区域服务类
 */
public interface RegionWareService extends IService<RegionWare> {
    /**
     * 获取开通区域-仓库列表
     *
     * @param pageParam         分页信息
     * @param regionWareQueryVo 查询信息
     * @return 分页列表
     */
    IPage<RegionWare> selectPageInfo(Page<RegionWare> pageParam, RegionWareQueryVo regionWareQueryVo);

    /**
     * 新增开通区域
     * @param regionWare 入参
     */
    void saveRegionWare(RegionWare regionWare);

    /**
     * 取消开通区域
     * @param id 区域 ID
     * @param status 修改后的状态
     */
    void updateStatus(Long id, Integer status);
}
