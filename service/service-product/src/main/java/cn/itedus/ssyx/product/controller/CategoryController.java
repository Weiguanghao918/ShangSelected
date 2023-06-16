package cn.itedus.ssyx.product.controller;

import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.model.product.Category;
import cn.itedus.ssyx.product.service.CategoryService;
import cn.itedus.ssyx.vo.product.CategoryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-12 15:59
 * @description: 商品分类控制器
 */
@Api(tags = "商品分类管理")
@RestController
@RequestMapping("/admin/product/category")
@SuppressWarnings({"unchecked", "rawtypes"})
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @ApiOperation("获取商品分类分页列表")
    @GetMapping("/{page}/{limit}")
    public Result index(@PathVariable("page") Long page,
                        @PathVariable("limit") Long limit,
                        CategoryVo categoryVo) {
        Page<Category> pageParam = new Page<>(page, limit);
        IPage<Category> iPageModel = categoryService.selectPageInfo(pageParam, categoryVo);
        return Result.ok(iPageModel);
    }

    @ApiOperation("根据ID获取商品分类信息")
    @GetMapping("get/{id}")
    public Result getCategoryById(@PathVariable("id") Long id) {
        Category category = categoryService.getById(id);
        return Result.ok(category);
    }

    @ApiOperation("新增商品分类信息")
    @PostMapping("save")
    public Result saveCategory(@RequestBody Category category) {
        categoryService.save(category);
        return Result.ok();
    }

    @ApiOperation("修改商品信息")
    @PutMapping("update")
    public Result updateCategory(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.ok();
    }

    @ApiOperation("删除商品分类信息")
    @DeleteMapping("remove/{id}")
    public Result removeCategoryById(@PathVariable("id") Long id) {
        categoryService.removeById(id);
        return Result.ok();
    }

    @ApiOperation("根据Id列表批量删除商品分类信息")
    @DeleteMapping("batchRemove")
    public Result batchRemoveCategory(@RequestBody List<Long> idList) {
        categoryService.removeByIds(idList);
        return Result.ok();
    }

    @ApiOperation("获取全部商品分类")
    @GetMapping("findAllList")
    public Result findAllList() {
        List<Category> categoryList = categoryService.findAllList();
        return Result.ok(categoryList);
    }
}
