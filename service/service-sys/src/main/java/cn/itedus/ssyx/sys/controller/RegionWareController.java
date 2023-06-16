package cn.itedus.ssyx.sys.controller;

import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.model.sys.RegionWare;
import cn.itedus.ssyx.sys.service.RegionWareService;
import cn.itedus.ssyx.vo.sys.RegionWareQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-09 15:50
 * @description: 区域 -仓库控制器
 */
@Api(tags = "RegionWare管理")
@RestController
@RequestMapping("/admin/sys/regionWare")
@SuppressWarnings({"unchecked", "rawtypes"})
public class RegionWareController {
    @Autowired
    private RegionWareService regionWareService;

    @ApiOperation("获取开通区域列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable("page") Long page,
                        @PathVariable("limit") Long limit,
                        RegionWareQueryVo regionWareQueryVo) {
        Page<RegionWare> pageParam = new Page<>(page, limit);
        IPage<RegionWare> iPageModel = regionWareService.selectPageInfo(pageParam, regionWareQueryVo);
        return Result.ok(iPageModel);
    }

    @ApiOperation("新增开通区域")
    @PostMapping("save")
    public Result save(@RequestBody RegionWare regionWare) {
        regionWareService.saveRegionWare(regionWare);
        return Result.ok();
    }

    @ApiOperation("删除开通区域")
    @DeleteMapping("remove/{id}")
    public Result removeById(@PathVariable("id") Long id) {
        regionWareService.removeById(id);
        return Result.ok();
    }

    @ApiOperation("取消开通区域")
    @PostMapping("updateStatus/{id}/{status}")
    public Result updateState(@PathVariable("id") Long id,
                              @PathVariable("status") Integer status) {
        regionWareService.updateStatus(id, status);
        return Result.ok();
    }


}
