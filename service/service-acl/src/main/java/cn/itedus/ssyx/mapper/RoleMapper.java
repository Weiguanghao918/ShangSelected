package cn.itedus.ssyx.mapper;

import cn.itedus.ssyx.model.acl.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-08 16:14
 * @description: 角色管理Mapper
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}
