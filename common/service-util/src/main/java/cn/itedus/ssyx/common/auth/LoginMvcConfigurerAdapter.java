package cn.itedus.ssyx.common.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-19 18:22
 * @description: 设定自定义拦截路径
 */
@Configuration
public class LoginMvcConfigurerAdapter extends WebMvcConfigurationSupport {
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //添加自定义拦截器，设置路径
        registry.addInterceptor(new UserLoginInterceptor(redisTemplate))
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/user/weixin/wxLogin/*");
        super.addInterceptors(registry);
    }
}
