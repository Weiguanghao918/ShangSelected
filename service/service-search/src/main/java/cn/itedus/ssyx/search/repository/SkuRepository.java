package cn.itedus.ssyx.search.repository;

import cn.itedus.ssyx.model.search.SkuEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-14 16:53
 * @description: ES数据仓储类来操作ES数据库
 */
public interface SkuRepository extends ElasticsearchRepository<SkuEs, Long> {
}
