package cn.itedus.ssyx.service.impl;

import cn.itedus.ssyx.helper.PermissionHelper;
import cn.itedus.ssyx.mapper.PermissionMapper;
import cn.itedus.ssyx.model.acl.Permission;
import cn.itedus.ssyx.service.PermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-09 14:57
 * @description: 菜单接口实现类
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> queryAllMenu() {

        //1. 查询所有的菜单数据，方便后续封装
        List<Permission> permissionList = permissionMapper.selectList(null);
        //2. 获取子节点,构建树形结构
        List<Permission> result = PermissionHelper.builder(permissionList);

        return result;
    }

    @Override
    public boolean removeChildById(Long id) {
        List<Long> idList = new ArrayList<>();
        this.selectChildListById(id, idList);
        permissionMapper.deleteBatchIds(idList);
        return true;
    }

    /**
     * 递归获取子节点
     *
     * @param id     当前节点ID
     * @param idList 子节点ID列表
     */
    private void selectChildListById(Long id, List<Long> idList) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", id);
        queryWrapper.select("id");
        List<Permission> permissionList = permissionMapper.selectList(queryWrapper);
        permissionList.stream().forEach(item -> {
            idList.add(item.getId());
            this.selectChildListById(item.getId(), idList);
        });
    }
}
