package cn.itedus.ssyx.controller;

import cn.itedus.ssyx.common.result.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-08 16:08
 * @description: 登陆控制器
 */
@RestController
@RequestMapping("/admin/acl/index")
public class IndexController {

    /**
     * 1、请求登陆的login
     */
    @PostMapping("login")
    public Result login() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", "admin-token");
        return Result.ok(map);
    }

    /**
     * 2 获取用户信息
     */
    @GetMapping("info")
    public Result info() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Komorebi");
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return Result.ok(map);
    }

    /**
     * 3 退出
     */
    @PostMapping("logout")
    public Result logout() {
        return Result.ok();
    }

}