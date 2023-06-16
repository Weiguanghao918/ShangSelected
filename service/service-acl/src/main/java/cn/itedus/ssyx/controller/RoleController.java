package cn.itedus.ssyx.controller;

import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.model.acl.Role;
import cn.itedus.ssyx.service.RoleService;
import cn.itedus.ssyx.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-08 16:12
 * @description: 角色管理控制器
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/acl/role")
@Slf4j
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "获取角色分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@ApiParam(name = "page", value = "当前页码", required = true) @PathVariable("page") Long page,
                        @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable("limit") Long limit,
                        @ApiParam(name = "roleQueryVo", value = "查询对象", required = false) RoleQueryVo roleQueryVo) {
        Page<Role> pageParam = new Page(page, limit);
        IPage<Role> pageModel = roleService.selectPage(pageParam, roleQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("获取角色")
    @GetMapping("get/{id}")
    public Result getRole(@PathVariable("id") Long id) {
        Role role = roleService.getRoleById(id);
        return Result.ok(role);
    }

    @ApiOperation("新增角色")
    @PostMapping("save")
    public Result addRole(@RequestBody Role role) {
        roleService.save(role);
        return Result.ok();
    }

    @ApiOperation("修改角色")
    @PutMapping("update")
    public Result updateById(@RequestBody Role role) {
        roleService.updateById(role);
        return Result.ok();
    }

    @ApiOperation("删除角色")
    @DeleteMapping("remove/{id}")
    public Result removeRoleById(@PathVariable("id") Long id) {
        roleService.removeById(id);
        return Result.ok();
    }

    @ApiOperation("根据Id列表删除角色")
    @DeleteMapping("batchRemove")
    public Result batchRemoveByIds(@RequestBody List<Long> idList) {
        roleService.removeByIds(idList);
        return Result.ok();
    }
}

