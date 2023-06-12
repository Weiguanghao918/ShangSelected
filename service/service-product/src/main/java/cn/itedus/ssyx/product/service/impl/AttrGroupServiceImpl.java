package cn.itedus.ssyx.product.service.impl;

import cn.itedus.ssyx.model.product.AttrGroup;
import cn.itedus.ssyx.product.mapper.AttrGroupMapper;
import cn.itedus.ssyx.product.service.AttrGroupService;
import cn.itedus.ssyx.vo.product.AttrGroupQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-12 16:26
 * @description: 平台属性分组服务实现类
 */
@Service
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupMapper, AttrGroup> implements AttrGroupService {
    @Autowired
    private AttrGroupMapper attrGroupMapper;

    @Override
    public IPage<AttrGroup> selectAttrGroupPage(Page<AttrGroup> pageParam, AttrGroupQueryVo attrGroupQueryVo) {
        LambdaQueryWrapper<AttrGroup> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(attrGroupQueryVo.getName())) {
            lambdaQueryWrapper.like(AttrGroup::getName, attrGroupQueryVo.getName());
        }
        IPage<AttrGroup> iPageModel = attrGroupMapper.selectPage(pageParam, lambdaQueryWrapper);
        return iPageModel;
    }

    @Override
    public List<AttrGroup> findAllList() {
        List<AttrGroup> attrGroupList = attrGroupMapper.selectList(null);
        return attrGroupList;
    }
}
