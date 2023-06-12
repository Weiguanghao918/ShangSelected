package cn.itedus.ssyx.product.service.impl;

import cn.itedus.ssyx.model.product.Attr;
import cn.itedus.ssyx.product.mapper.AttrMapper;
import cn.itedus.ssyx.product.service.AttrService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-12 16:41
 * @description: 平台属性服务实现类
 */
@Service
public class AttrServiceImpl extends ServiceImpl<AttrMapper, Attr> implements AttrService {
    @Autowired
    private AttrMapper attrMapper;

    @Override
    public List<Attr> findByAttrGroupId(Long attrGroupId) {
        LambdaQueryWrapper<Attr> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Attr::getAttrGroupId, attrGroupId);
        List<Attr> attrList = attrMapper.selectList(lambdaQueryWrapper);
        return attrList;
    }
}
