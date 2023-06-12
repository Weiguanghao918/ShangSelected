package cn.itedus.ssyx.sys.service;

import cn.itedus.ssyx.model.sys.Region;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-09 15:51
 * @description: 区域服务类
 */
public interface RegionService extends IService<Region> {
    /**
     * 根据关键字获取区域列表
     * @param keyWord 关键字
     * @return 区域信息列表
     */
    List<Region> findRegionByKeyword(String keyWord);
}
