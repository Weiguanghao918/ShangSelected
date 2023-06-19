package cn.itedus.ssyx.home.controller;

import cn.itedus.ssyx.common.auth.AuthContextHolder;
import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.home.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-19 19:13
 * @description: 小程序首页接口
 */
@Api(tags = "首页接口")
@RestController
@RequestMapping("/api/home")
public class HomeApiController {

    @Autowired
    private HomeService homeService;

    @ApiOperation("获取首页数据")
    @GetMapping("index")
    public Result index(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId();
        Map<String, Object> map = homeService.homeData(userId);
        return Result.ok(map);
    }
}
