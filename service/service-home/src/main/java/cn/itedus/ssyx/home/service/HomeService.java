package cn.itedus.ssyx.home.service;

import java.util.Map;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-19 19:13
 * @description: 首页服务接口
 */
public interface HomeService {
    /**
     * 获取首页数据
     * @param userId 用户ID
     * @return 首页数据map
     */
    Map<String, Object> homeData(Long userId);
}
