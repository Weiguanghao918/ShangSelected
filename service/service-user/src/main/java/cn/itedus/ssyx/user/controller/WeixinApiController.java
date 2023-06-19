package cn.itedus.ssyx.user.controller;

import cn.itedus.ssyx.common.constant.RedisConst;
import cn.itedus.ssyx.common.exception.SsyxException;
import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.common.result.ResultCodeEnum;
import cn.itedus.ssyx.common.auth.AuthContextHolder;
import cn.itedus.ssyx.common.utils.JwtHelper;
import cn.itedus.ssyx.enums.UserType;
import cn.itedus.ssyx.model.user.User;
import cn.itedus.ssyx.user.service.UserService;
import cn.itedus.ssyx.user.utils.ConstantPropertiesUtil;
import cn.itedus.ssyx.user.utils.HttpClientUtils;
import cn.itedus.ssyx.vo.user.LeaderAddressVo;
import cn.itedus.ssyx.vo.user.UserLoginVo;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-16 16:56
 * @description: 微信小程序登录接口
 */
@RestController
@RequestMapping("/api/user/weixin")
public class WeixinApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;
    private Logger logger = LoggerFactory.getLogger(WeixinApiController.class);

    @ApiOperation("微信登录获取openid(小程序)")
    @GetMapping("/wxLogin/{code}")
    public Result callback(@PathVariable("code") String code) {
        //获取临时票据code
        logger.info("微信授权服务器回调.......{}", code);

        if (StringUtils.isEmpty(code)) {
            throw new SsyxException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //使用code + appid 和 appSecret获取access_token
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/jscode2session")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&js_code=%s")
                .append("&grant_type=authorization_code");

        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(), ConstantPropertiesUtil.WX_OPEN_APP_ID, ConstantPropertiesUtil.WX_OPEN_APP_SECRET, code);
        String result = null;
        try {
            result = HttpClientUtils.get(accessTokenUrl);
        } catch (Exception e) {
            throw new SsyxException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        logger.info("使用code获取的access_token结果：{}", result);

        JSONObject resultObject = JSONObject.parseObject(result);
        if (resultObject.get("errcode") != null) {
            throw new SsyxException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        String accessToken = resultObject.getString("session_key");
        String openId = resultObject.getString("openid");

        //根据openId查询用户是否已经登陆过，有数据
        User user = userService.getByOpenId(openId);
        //如果没有查询到用户，则插入新数据
        if (user == null) {
            user = new User();
            user.setOpenId(openId);
            user.setNickName(openId);
            user.setPhone("");
            user.setUserType(UserType.USER);
            user.setIsNew(0);
            userService.save(user);
        }

        //这里为了首页展示，还需要查询出和用户相关的地址信息和绑定的团长信息
        LeaderAddressVo leaderAddressVo = userService.getLeadAddressVoByUserId(user.getId());
        Map<String, Object> map = new HashMap<>();
        String name = user.getNickName();
        String token = JwtHelper.createToken(user.getId(), name);

        //获取当前登录用户信息，放到Redis里面，设置有效时间
        UserLoginVo userLoginVo = userService.getUserLoginVoByUserId(user.getId());
        redisTemplate.opsForValue().set(RedisConst.USER_LOGIN_KEY_PREFIX + user.getId(), userLoginVo,
                RedisConst.USERKEY_TIMEOUT, TimeUnit.DAYS);
        map.put("user", user);
        map.put("leaderAddressVo", leaderAddressVo);
        map.put("token", token);

        return Result.ok(map);

    }

    @ApiOperation("更新用户昵称与头像")
    @PostMapping("/auth/updateUser")
    public Result updateUser(@RequestBody User user) {
        User user1 = userService.getById(AuthContextHolder.getUserId());
        //把昵称更新为微信用户
        user1.setNickName(user.getNickName().replaceAll("[ue000-uefff]", "*"));
        user1.setPhotoUrl(user.getPhotoUrl());
        userService.updateById(user1);
        return Result.ok(null);
    }


}
