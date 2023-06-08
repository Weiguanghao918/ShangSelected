package cn.itedus.ssyx.service;

import cn.itedus.ssyx.model.acl.Role;
import cn.itedus.ssyx.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-08 16:14
 * @description: 角色管理Service
 */
public interface RoleService extends IService<Role> {
    /**
     * 获取角色分页数据
     * @param pageParam 分页对象
     * @param roleQueryVo 查询对象
     * @return 结果集
     */
    IPage<Role> selectPage(Page<Role> pageParam, RoleQueryVo roleQueryVo);

    /**
     * 根据Id获取角色
     * @param id 角色Id
     * @return 角色信息
     */
    Role getRoleById(Long id);
}
