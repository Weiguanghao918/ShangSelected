package cn.itedus.ssyx.activity.controller;

import cn.itedus.ssyx.activity.service.CouponInfoService;
import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.model.activity.CouponInfo;
import cn.itedus.ssyx.vo.activity.CouponRuleVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-15 14:48
 * @description: 优惠券管理控制器
 */
@Api(tags = "优惠券管理控制器")
@RestController
@RequestMapping("/admin/activity/couponInfo")
public class CouponInfoController {
    @Autowired
    private CouponInfoService couponInfoService;

    @ApiOperation("获取优惠券信息分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable("page") Long page,
                        @PathVariable("limit") Long limit) {
        Page<CouponInfo> pageParam = new Page<>(page, limit);
        IPage<CouponInfo> iPageModel = couponInfoService.selectPage(pageParam);
        return Result.ok(iPageModel);
    }

    @ApiOperation("根据Id获取优惠券")
    @GetMapping("get/{id}")
    public Result getCouponById(@PathVariable("id") Long id) {
        CouponInfo couponInfo = couponInfoService.getCouponInfo(id);
        return Result.ok(couponInfo);
    }

    @ApiOperation("新增优惠券")
    @PostMapping("save")
    public Result save(@RequestBody CouponInfo couponInfo) {
        couponInfoService.save(couponInfo);
        return Result.ok();
    }

    @ApiOperation("修改优惠券")
    @PutMapping("update")
    public Result updateById(@RequestBody CouponInfo couponInfo) {
        couponInfoService.updateById(couponInfo);
        return Result.ok();
    }

    @ApiOperation("删除优惠券")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable("id") Long id) {
        couponInfoService.removeById(id);
        return Result.ok();
    }

    @ApiOperation("批量删除优惠券")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        couponInfoService.removeByIds(idList);
        return Result.ok();
    }

    @ApiOperation("根据Id获取优惠券信息规则列表")
    @GetMapping("findCouponRuleList/{id}")
    public Result findCouponRuleList(@PathVariable("id") Long id) {
        Map<String, Object> map = couponInfoService.findCouponRuleList(id);
        return Result.ok(map);
    }

    @ApiOperation("新增优惠券规则")
    @PostMapping("saveCouponRule")
    public Result saveCouponRule(@RequestBody CouponRuleVo couponRuleVo) {
        couponInfoService.saveCouponRule(couponRuleVo);
        return Result.ok();
    }

    @ApiOperation("根据关键字获取优惠券列表，活动使用")
    @GetMapping("findCouponByKeyword/{keyword}")
    public Result findCouponByKeyword(@PathVariable("keyword") String keyword) {
        List<CouponInfo> couponInfoList = couponInfoService.findCouponByKeyword(keyword);
        return Result.ok(couponInfoList);
    }

}
