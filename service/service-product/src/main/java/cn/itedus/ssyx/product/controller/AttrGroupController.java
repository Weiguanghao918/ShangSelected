package cn.itedus.ssyx.product.controller;

import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.model.product.AttrGroup;
import cn.itedus.ssyx.product.service.AttrGroupService;
import cn.itedus.ssyx.vo.product.AttrGroupQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-12 16:22
 * @description: 平台属性分组管理
 */
@Api(tags = "平台属性分组管理")
@RestController
@RequestMapping("/admin/product/attrGroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @ApiOperation("获取平台属性分组分页列表")
    @GetMapping("/{page}/{limit}")
    public Result index(@PathVariable("page") Long page,
                        @PathVariable("limit") Long limit,
                        AttrGroupQueryVo attrGroupQueryVo) {
        Page<AttrGroup> pageParam = new Page<>(page, limit);
        IPage<AttrGroup> iPageModel = attrGroupService.selectAttrGroupPage(pageParam, attrGroupQueryVo);
        return Result.ok(iPageModel);
    }

    @ApiOperation("根据Id获取平台属性分组内容")
    @GetMapping("get/{id}")
    public Result getAttrGroupById(@PathVariable("id") Long id) {
        AttrGroup attrGroup = attrGroupService.getById(id);
        return Result.ok(attrGroup);
    }

    @ApiOperation("新增平台属性分组信息")
    @PostMapping("save")
    public Result saveAttrGroup(@RequestBody AttrGroup attrGroup) {
        attrGroupService.save(attrGroup);
        return Result.ok();
    }

    @ApiOperation("修改平台属性分组信息")
    @PutMapping("update")
    public Result updateAttrGroup(@RequestBody AttrGroup attrGroup) {
        attrGroupService.updateById(attrGroup);
        return Result.ok();
    }

    @ApiOperation("删除平台属性分组")
    @DeleteMapping("remove/{id}")
    public Result removeAttrGroupById(@PathVariable("id") Long id) {
        attrGroupService.removeById(id);
        return Result.ok();
    }

    @ApiOperation("根据Id列表批量删除平台属性分组")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        attrGroupService.removeByIds(idList);
        return Result.ok();
    }

    @ApiOperation("获取全部平台属性分组信息")
    @GetMapping("findAllList")
    public Result findAllList() {
        List<AttrGroup> attrGroupList = attrGroupService.findAllList();
        return Result.ok(attrGroupList);
    }
}
