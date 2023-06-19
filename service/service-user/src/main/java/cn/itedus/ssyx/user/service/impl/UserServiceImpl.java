package cn.itedus.ssyx.user.service.impl;

import cn.itedus.ssyx.enums.UserType;
import cn.itedus.ssyx.model.user.Leader;
import cn.itedus.ssyx.model.user.User;
import cn.itedus.ssyx.model.user.UserDelivery;
import cn.itedus.ssyx.user.mapper.LeaderMapper;
import cn.itedus.ssyx.user.mapper.UserDeliveryMapper;
import cn.itedus.ssyx.user.mapper.UserMapper;
import cn.itedus.ssyx.user.service.UserService;
import cn.itedus.ssyx.vo.user.LeaderAddressVo;
import cn.itedus.ssyx.vo.user.UserLoginVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-16 17:02
 * @description: 微信端用户服务接口实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDeliveryMapper userDeliveryMapper;
    @Autowired
    private LeaderMapper leaderMapper;

    @Override
    public User getByOpenId(String openId) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getOpenId, openId));
        return user;
    }

    @Override
    public LeaderAddressVo getLeadAddressVoByUserId(Long userId) {
        LambdaQueryWrapper<UserDelivery> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserDelivery::getUserId, userId);
        lambdaQueryWrapper.eq(UserDelivery::getIsDefault, 1);
        UserDelivery userDelivery = userDeliveryMapper.selectOne(lambdaQueryWrapper);
        if (userDelivery == null) {
            return null;
        }
        Leader leader = leaderMapper.selectById(userDelivery.getLeaderId());
        LeaderAddressVo leaderAddressVo = new LeaderAddressVo();
        BeanUtils.copyProperties(leader, leaderAddressVo);
        leaderAddressVo.setUserId(userId);
        leaderAddressVo.setLeaderId(leader.getId());
        leaderAddressVo.setLeaderName(leader.getName());
        leaderAddressVo.setLeaderPhone(leader.getPhone());
        leaderAddressVo.setWareId(userDelivery.getWareId());
        leaderAddressVo.setStorePath(leader.getStorePath());
        return leaderAddressVo;
    }

    @Override
    public UserLoginVo getUserLoginVoByUserId(Long userId) {
        User user = userMapper.selectById(userId);
        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setPhotoUrl(user.getPhotoUrl());
        userLoginVo.setNickName(user.getNickName());
        userLoginVo.setOpenId(user.getOpenId());
        userLoginVo.setIsNew(user.getIsNew());
        userLoginVo.setUserId(userId);

        UserDelivery userDelivery = userDeliveryMapper.selectOne(
                new LambdaQueryWrapper<UserDelivery>().eq(UserDelivery::getUserId, userId)
                        .eq(UserDelivery::getIsDefault, 1)
        );
        if (userDelivery != null) {
            userLoginVo.setLeaderId(userDelivery.getLeaderId());
            userLoginVo.setWareId(userDelivery.getWareId());
        } else {
            userLoginVo.setLeaderId(1L);
            userLoginVo.setWareId(1L);
        }
        return userLoginVo;

    }
}
