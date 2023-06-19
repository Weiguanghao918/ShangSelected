package cn.itedus.ssyx.user.utils;

import cn.itedus.ssyx.common.utils.DesUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-16 17:09
 * @description: 获取配置工具类
 */
@Component
public class ConstantPropertiesUtil implements InitializingBean {

    @Value("${wx.open.app_id}")
    private String appId;
    @Value("${wx.open.app_secret}")
    private String appSecret;

    public static String WX_OPEN_APP_ID;
    public static String WX_OPEN_APP_SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        appId = DesUtils.decrypt(appId, DesUtils.Mode.DES, "abcdefgh");
        appSecret = DesUtils.decrypt(appSecret, DesUtils.Mode.DES, "abcdefgh");
        WX_OPEN_APP_ID = appId;
        WX_OPEN_APP_SECRET = appSecret;
    }
}
