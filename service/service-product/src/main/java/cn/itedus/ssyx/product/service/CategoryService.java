package cn.itedus.ssyx.product.service;

import cn.itedus.ssyx.model.product.Category;
import cn.itedus.ssyx.vo.product.CategoryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-12 16:01
 * @description: 商品分类服务接口
 */
public interface CategoryService extends IService<Category> {
    /**
     * 获取商品分类表分页结果
     *
     * @param pageParam  分页数据
     * @param categoryVo 查询数据
     * @return 分页结果
     */
    IPage<Category> selectPageInfo(Page<Category> pageParam, CategoryVo categoryVo);

    /**
     * 获取全部商品分类数据
     *
     * @return 商品分类信息列表
     */
    List<Category> findAllList();

    /**
     * 根据Id列表获取分类集合
     *
     * @param idList Id列表
     * @return 分类集合
     */
    List<Category> findCategoryList(List<Long> idList);
}
