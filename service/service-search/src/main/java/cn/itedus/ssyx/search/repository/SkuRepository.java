package cn.itedus.ssyx.search.repository;

import cn.itedus.ssyx.model.search.SkuEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.net.URLConnection;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-14 16:53
 * @description: ES数据仓储类来操作ES数据库
 */
public interface SkuRepository extends ElasticsearchRepository<SkuEs, Long> {
    /**
     * 获取热点商品
     *
     * @param pageable 分页数据
     * @return 热点商品列表
     */
    Page<SkuEs> findByOrderByHotScoreDesc(Pageable pageable);
}
