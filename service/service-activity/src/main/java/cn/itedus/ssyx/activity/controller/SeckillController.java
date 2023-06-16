package cn.itedus.ssyx.activity.controller;

import cn.itedus.ssyx.activity.service.SeckillService;
import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.model.activity.Seckill;
import cn.itedus.ssyx.vo.activity.SeckillQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-15 19:30
 * @description: 秒杀活动管理控制器
 */
@Api(tags = "秒杀活动管理控制器")
@RestController
@RequestMapping("/admin/activity/seckill")
@SuppressWarnings({"unchecked", "rawtypes"})
public class SeckillController {
    @Autowired
    private SeckillService seckillService;

    @ApiOperation("获取秒杀分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable("page") Long page,
                        @PathVariable("limit") Long limit,
                        SeckillQueryVo seckillQueryVo) {
        Page<Seckill> pageParam = new Page<>(page, limit);
        IPage<Seckill> iPageModel = seckillService.selectPage(pageParam, seckillQueryVo);
        return Result.ok(iPageModel);
    }

    @ApiOperation("根据Id获取秒杀信息")
    @GetMapping("get/{id}")
    public Result getSeckillById(@PathVariable("id") Long seckillId) {
        Seckill seckill = seckillService.getById(seckillId);
        return Result.ok(seckill);
    }

    @ApiOperation("新增秒杀活动")
    @PostMapping("save")
    public Result save(@RequestBody Seckill seckill) {
        seckillService.save(seckill);
        return Result.ok();
    }

    @ApiOperation("修改秒杀活动")
    @PutMapping("update")
    public Result updateSeckillById(@RequestBody Seckill seckill) {
        seckillService.updateById(seckill);
        return Result.ok();
    }


    @ApiOperation("删除秒杀活动")
    @DeleteMapping("remove/{id}")
    public Result removeById(@PathVariable("id") Long seckillId) {
        seckillService.removeById(seckillId);
        return Result.ok();
    }

    @ApiOperation("批量删除秒杀活动")
    @DeleteMapping("batchRemove")
    public Result batchRemoveSeckill(@RequestBody List<Long> idList){
        seckillService.removeByIds(idList);
        return Result.ok();
    }

    @ApiOperation("更新秒杀活动上下线状态")
    @PostMapping("updateStatus/{id}/{status}")
    public Result updateSeckillStatus(@PathVariable("id") Long id,
                                      @PathVariable("status") Integer status) {
        seckillService.updateSeckillStatus(id,status);
        return Result.ok();
    }
}
