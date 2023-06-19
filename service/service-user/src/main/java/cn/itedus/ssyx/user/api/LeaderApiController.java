package cn.itedus.ssyx.user.api;

import cn.itedus.ssyx.user.service.UserService;
import cn.itedus.ssyx.vo.user.LeaderAddressVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-19 19:00
 * @description: 团长接口
 */
@Api(tags = "团长接口")
@RestController
@RequestMapping("/api/user/leader")
public class LeaderApiController {
    @Autowired
    private UserService userService;

    @ApiOperation("获取提货点地址信息")
    @GetMapping("/inner/getUserAddressByUserId/{userId}")
    public LeaderAddressVo getUserAddressByUserId(@PathVariable("userId") Long userId) {
        LeaderAddressVo leaderAddressVo = userService.getLeadAddressVoByUserId(userId);
        return leaderAddressVo;
    }
}
