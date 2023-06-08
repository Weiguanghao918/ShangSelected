package cn.itedus.ssyx.mapper;

import cn.itedus.ssyx.model.acl.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-08 16:48
 * @description: 用户管理Mapper
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}
