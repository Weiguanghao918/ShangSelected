package cn.itedus.ssyx.user.service;

import cn.itedus.ssyx.model.user.User;
import cn.itedus.ssyx.vo.user.LeaderAddressVo;
import cn.itedus.ssyx.vo.user.UserLoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-16 17:02
 * @description: 微信端用户服务接口
 */
public interface UserService extends IService<User> {
    /**
     * 根据openId查询是否已经有登陆记录
     *
     * @param openId 用户openId
     * @return user记录
     */
    User getByOpenId(String openId);

    /**
     * 根据用户id查询已经存在的地址信息和团长信息，做页面展示
     *
     * @param userId 用户ID
     * @return 团长-地址vo类
     */
    LeaderAddressVo getLeadAddressVoByUserId(Long userId);

    /**
     * 根据用户Id获取用户完整信息，用于存入redis
     *
     * @param userId 用户ID
     * @return 用户信息vo类
     */
    UserLoginVo getUserLoginVoByUserId(Long userId);
}
