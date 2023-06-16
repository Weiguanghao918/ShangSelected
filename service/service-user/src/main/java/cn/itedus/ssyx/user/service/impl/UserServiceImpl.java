package cn.itedus.ssyx.user.service.impl;

import cn.itedus.ssyx.model.user.User;
import cn.itedus.ssyx.user.mapper.UserMapper;
import cn.itedus.ssyx.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-16 17:02
 * @description: 微信端用户服务接口实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
