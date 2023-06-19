package cn.itedus.ssyx.client.user;

import cn.itedus.ssyx.vo.user.LeaderAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-19 19:28
 * @description:
 */
@FeignClient("service-user")
public interface UserFeignClient {
    /**
     * 根据用户Id获取提货团长-地址信息
     *
     * @param userId 用户Id
     * @return 团长-地址vo类
     */
    @GetMapping("/api/user/leader/inner/getUserAddressByUserId/{userId}")
    public LeaderAddressVo getUserAddressByUserId(@PathVariable("userId") Long userId);
}
