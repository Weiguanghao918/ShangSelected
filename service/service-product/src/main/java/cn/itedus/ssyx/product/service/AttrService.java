package cn.itedus.ssyx.product.service;

import cn.itedus.ssyx.model.product.Attr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-12 16:41
 * @description: 平台属性服务接口
 */
public interface AttrService extends IService<Attr> {
    /**
     * 根据属性分组获取平台属性列表
     *
     * @param attrGroupId 属性分组Id
     * @return 平台属性列表
     */
    List<Attr> findByAttrGroupId(Long attrGroupId);
}
