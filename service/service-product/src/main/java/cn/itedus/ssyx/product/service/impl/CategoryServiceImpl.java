package cn.itedus.ssyx.product.service.impl;

import cn.itedus.ssyx.model.product.Category;
import cn.itedus.ssyx.product.mapper.CategoryMapper;
import cn.itedus.ssyx.product.service.CategoryService;
import cn.itedus.ssyx.vo.product.CategoryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-12 16:01
 * @description: 商品分类服务实现类
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public IPage<Category> selectPageInfo(Page<Category> pageParam, CategoryVo categoryVo) {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(categoryVo.getName())) {
            lambdaQueryWrapper.like(Category::getName, categoryVo.getName());
        }
        IPage<Category> iPageModel = categoryMapper.selectPage(pageParam, lambdaQueryWrapper);
        return iPageModel;
    }

    @Override
    public List<Category> findAllList() {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        List<Category> categoryList = categoryMapper.selectList(lambdaQueryWrapper);
        return categoryList;
    }

    @Override
    public List<Category> findCategoryList(List<Long> idList) {
        List<Category> categoryList = categoryMapper.selectBatchIds(idList);
        return categoryList;
    }
}
