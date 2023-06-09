package cn.itedus.ssyx.service;

import cn.itedus.ssyx.model.acl.Permission;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;


/**
 * @author: Guanghao Wei
 * @date: 2023-06-09 14:57
 * @description: 菜单Service接口
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 获取菜单列表，树状结构
     *
     * @return 菜单集合
     */
    List<Permission> queryAllMenu();

    /**
     * 递归删除菜单
     *
     * @param id 删除菜单ID
     */
    boolean removeChildById(Long id);
}
