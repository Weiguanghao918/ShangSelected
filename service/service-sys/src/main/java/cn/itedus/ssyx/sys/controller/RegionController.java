package cn.itedus.ssyx.sys.controller;

import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.sys.service.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-09 15:50
 * @description: 区域控制器
 */
@Api(tags = "区域接口")
@RestController
@RequestMapping("/admin/sys/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @ApiOperation("根据关键字获取地区列表")
    @GetMapping("findRegionByKeyword/{keyword}")
    public Result findRegionByKeyword(@PathVariable("keyword") String keyWord) {
        return Result.ok(regionService.findRegionByKeyword(keyWord));
    }
}
