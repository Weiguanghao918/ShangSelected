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

    /**
     * 根据分类Id和仓库Id获取商品SkuEs分页数据
     *
     * @param categoryId 分类Id
     * @param wareId     仓库Id
     * @param pageable   分页信息
     * @return 商品SkuEs分页数据
     */
    Page<SkuEs> findByCategoryIdAndWareId(Long categoryId, Long wareId, Pageable pageable);

    /**
     * 根据关键字和仓库Id获取商品SkuEs分页数据
     *
     * @param keyword  关键字
     * @param wareId   仓库Id
     * @param pageable 分页信息
     * @return 商品SkuEs分页数据
     */
    Page<SkuEs> findByKeywordAndWareId(String keyword, Long wareId, Pageable pageable);
}
