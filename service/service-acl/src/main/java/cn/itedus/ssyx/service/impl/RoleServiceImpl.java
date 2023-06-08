package cn.itedus.ssyx.service.impl;

import cn.itedus.ssyx.mapper.RoleMapper;
import cn.itedus.ssyx.model.acl.Role;
import cn.itedus.ssyx.service.RoleService;
import cn.itedus.ssyx.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-08 16:15
 * @description: 角色管理Service实习类
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public IPage<Role> selectPage(Page<Role> pageParam, RoleQueryVo roleQueryVo) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(roleQueryVo.getRoleName())) {
            queryWrapper.like("role_name", roleQueryVo.getRoleName());
        }

        IPage<Role> pageResult = roleMapper.selectPage(pageParam, queryWrapper);
        return pageResult;
    }

    @Override
    public Role getRoleById(Long id) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Role role = roleMapper.selectOne(queryWrapper);
        return role;
    }
}
