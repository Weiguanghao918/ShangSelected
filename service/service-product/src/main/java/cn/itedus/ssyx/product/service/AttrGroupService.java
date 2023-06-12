package cn.itedus.ssyx.product.service;

import cn.itedus.ssyx.model.product.AttrGroup;
import cn.itedus.ssyx.vo.product.AttrGroupQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-12 16:25
 * @description: 平台熟悉分组服务接口
 */
public interface AttrGroupService extends IService<AttrGroup> {
    /**
     * 获取平台属性分组分页列表
     *
     * @param pageParam        分页信息
     * @param attrGroupQueryVo 查询条件
     * @return 分页列表
     */
    IPage<AttrGroup> selectAttrGroupPage(Page<AttrGroup> pageParam, AttrGroupQueryVo attrGroupQueryVo);

    /**
     * 获取全部平台属性分组信息
     *
     * @return 分组列表
     */
    List<AttrGroup> findAllList();
}
