package cn.itedus.ssyx.activity.controller;

import cn.itedus.ssyx.activity.service.ActivityInfoService;
import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.model.activity.ActivityInfo;
import cn.itedus.ssyx.model.activity.ActivityRule;
import cn.itedus.ssyx.model.product.SkuInfo;
import cn.itedus.ssyx.vo.activity.ActivityRuleVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-15 14:48
 * @description: 营销活动控制器
 */
@Api(tags = "营销活动控制器")
@RestController
@RequestMapping("/admin/activity/activityInfo")
public class ActivityInfoController {
    @Autowired
    private ActivityInfoService activityInfoService;

    @ApiOperation("获取活动分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable("page") Long page,
                        @PathVariable("limit") Long limit) {
        Page<ActivityInfo> pageParam = new Page<>(page, limit);
        IPage<ActivityInfo> iPageModel = activityInfoService.selectPage(pageParam);
        return Result.ok(iPageModel);
    }

    @ApiOperation("根据Id获取活动")
    @GetMapping("get/{id}")
    public Result get(@PathVariable("id") Long id) {
        ActivityInfo activityInfo = activityInfoService.getById(id);
        activityInfo.setActivityTypeString(activityInfo.getActivityType().getComment());
        return Result.ok(activityInfo);
    }


    @ApiOperation(value = "修改活动")
    @PutMapping("update")
    public Result updateById(@RequestBody ActivityInfo activityInfo) {
        activityInfoService.updateById(activityInfo);
        return Result.ok();
    }

    @ApiOperation(value = "删除活动")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        activityInfoService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "根据id列表删除活动")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<String> idList) {
        activityInfoService.removeByIds(idList);
        return Result.ok();
    }

    @ApiOperation("获取活动规则")
    @GetMapping("findActivityRuleList/{id}")
    public Result findActivityRuleList(@PathVariable("id") Long activityId) {
        Map<String, Object> map = activityInfoService.findActivityRuleList(activityId);
        return Result.ok(map);
    }

    @ApiOperation("新增活动规则")
    @PostMapping("saveActivityRule")
    public Result saveActivityRule(@RequestBody ActivityRuleVo activityRuleVo) {
        activityInfoService.saveActivityRule(activityRuleVo);
        return Result.ok();
    }

    @ApiOperation("根据关键字获取Sku列表，活动使用")
    @GetMapping("findSkuInfoByKeyword/{keyword}")
    public Result findSkuInfoByKeyword(@PathVariable("keyword") String keyword) {
        List<SkuInfo> skuInfoList = activityInfoService.findSkuInfoByKeyword(keyword);
        return Result.ok(skuInfoList);
    }
}
