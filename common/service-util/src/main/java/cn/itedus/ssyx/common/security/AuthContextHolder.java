package cn.itedus.ssyx.common.security;

import cn.itedus.ssyx.vo.acl.AdminLoginVo;
import cn.itedus.ssyx.vo.user.UserLoginVo;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-19 18:15
 * @description: 获取登录用户信息类
 */
public class AuthContextHolder {
    //会员用户id
    private static ThreadLocal<Long> userId = new ThreadLocal<Long>();
    //仓库id
    private static ThreadLocal<Long> wareId = new ThreadLocal<>();
    //会员基本信息
    private static ThreadLocal<UserLoginVo> userLoginVo = new ThreadLocal<>();

    //后台管理用户id
    private static ThreadLocal<Long> adminId = new ThreadLocal<Long>();
    //管理员基本信息
    private static ThreadLocal<AdminLoginVo> adminLoginVo = new ThreadLocal<>();

    public static ThreadLocal<Long> getUserId() {
        return userId;
    }

    public static void setUserId(ThreadLocal<Long> userId) {
        AuthContextHolder.userId = userId;
    }

    public static ThreadLocal<Long> getWareId() {
        return wareId;
    }

    public static void setWareId(ThreadLocal<Long> wareId) {
        AuthContextHolder.wareId = wareId;
    }

    public static ThreadLocal<UserLoginVo> getUserLoginVo() {
        return userLoginVo;
    }

    public static void setUserLoginVo(ThreadLocal<UserLoginVo> userLoginVo) {
        AuthContextHolder.userLoginVo = userLoginVo;
    }

    public static ThreadLocal<Long> getAdminId() {
        return adminId;
    }

    public static void setAdminId(ThreadLocal<Long> adminId) {
        AuthContextHolder.adminId = adminId;
    }

    public static ThreadLocal<AdminLoginVo> getAdminLoginVo() {
        return adminLoginVo;
    }

    public static void setAdminLoginVo(ThreadLocal<AdminLoginVo> adminLoginVo) {
        AuthContextHolder.adminLoginVo = adminLoginVo;
    }
}
