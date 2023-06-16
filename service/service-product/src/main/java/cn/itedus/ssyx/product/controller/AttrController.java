package cn.itedus.ssyx.product.controller;

import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.model.product.Attr;
import cn.itedus.ssyx.product.service.AttrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-12 16:39
 * @description: 平台属性管理
 */
@Api(tags = "平台属性管理")
@RestController
@RequestMapping("/admin/product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @ApiOperation("根据属性分组获取平台属性列表")
    @GetMapping("{attrGroupId}")
    public Result index(@PathVariable("attrGroupId") Long attrGroupId) {
        List<Attr> attrList = attrService.findByAttrGroupId(attrGroupId);
        return Result.ok(attrList);
    }

    @ApiOperation("根据属性Id获取平台属性")
    @GetMapping("get/{id}")
    public Result getById(@PathVariable("id") Long id) {
        Attr attr = attrService.getById(id);
        return Result.ok(attr);
    }

    @ApiOperation("新增平台属性")
    @PostMapping("save")
    public Result save(@RequestBody Attr attr) {
        attrService.save(attr);
        return Result.ok();
    }

    @ApiOperation("修改平台属性")
    @PutMapping("update")
    public Result updateAttr(@RequestBody Attr attr) {
        attrService.updateById(attr);
        return Result.ok();
    }

    @ApiOperation("删除平台属性")
    @DeleteMapping("remove/{id}")
    public Result removeByAttrId(@PathVariable("id") Long id) {
        attrService.removeById(id);
        return Result.ok();
    }

    @ApiOperation("根据Id列表批量删除平台属性")
    @DeleteMapping("batchRemove")
    public Result batchRemoveAttr(@RequestBody List<Long> idList) {
        attrService.removeByIds(idList);
        return Result.ok();
    }
}
