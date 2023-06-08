package cn.itedus.ssyx.service;

import cn.itedus.ssyx.model.acl.Admin;
import cn.itedus.ssyx.vo.acl.AdminQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-08 16:48
 * @description: 用户管理Service
 */
public interface AdminService extends IService<Admin> {
    /**
     * 分页查询用户信息
     * @param pageParam 分页信息
     * @param adminQueryVo 查询条件
     * @return 分页结果
     */
    IPage<Admin> selectPage(Page<Admin> pageParam, AdminQueryVo adminQueryVo);
}
