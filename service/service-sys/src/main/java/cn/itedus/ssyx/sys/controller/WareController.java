package cn.itedus.ssyx.sys.controller;

import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.sys.service.WareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-09 15:51
 * @description: 仓库管理控制器
 */
@Api(tags = "Ware管理")
@RestController
@RequestMapping("/admin/sys/ware")
public class WareController {
    @Autowired
    private WareService wareService;

    @ApiOperation("获取全部仓库")
    @GetMapping("findAllList")
    public Result findAllList(){
        return Result.ok(wareService.list());
    }
}
