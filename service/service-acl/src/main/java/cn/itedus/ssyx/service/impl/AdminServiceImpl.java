package cn.itedus.ssyx.service.impl;

import cn.itedus.ssyx.mapper.AdminMapper;
import cn.itedus.ssyx.model.acl.Admin;
import cn.itedus.ssyx.service.AdminService;
import cn.itedus.ssyx.vo.acl.AdminQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-08 16:49
 * @description: 用户管理Service实现类
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public IPage<Admin> selectPage(Page<Admin> pageParam, AdminQueryVo adminQueryVo) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(adminQueryVo.getUsername())) {
            queryWrapper.like(Admin::getName, adminQueryVo.getUsername());
        }

        IPage<Admin> ipageResult = adminMapper.selectPage(pageParam, queryWrapper);
        return ipageResult;
    }
}
