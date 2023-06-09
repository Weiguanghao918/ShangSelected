package cn.itedus.ssyx.mapper;

import cn.itedus.ssyx.model.acl.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-09 14:56
 * @description: 菜单Mapper接口
 */
@Mapper
public interface PermissionMapper  extends BaseMapper<Permission> {
}
