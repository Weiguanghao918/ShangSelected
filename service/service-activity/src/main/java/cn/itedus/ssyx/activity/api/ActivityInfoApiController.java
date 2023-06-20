package cn.itedus.ssyx.activity.api;

import cn.itedus.ssyx.activity.service.ActivityInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-20 15:44
 * @description: 活动内部远程调用接口
 */
@Api("促销与优惠券接口")
@RestController
@RequestMapping("/api/activity")
public class ActivityInfoApiController {
    @Autowired
    private ActivityInfoService activityInfoService;

    @ApiOperation("根据SkuId列表获取促销信息")
    @PostMapping("inner/findActivity")
    public Map<Long, List<String>> findActivity(@RequestBody List<Long> skuIdList) {
        Map<Long, List<String>> map = activityInfoService.findActivity(skuIdList);
        return map;
    }
}
