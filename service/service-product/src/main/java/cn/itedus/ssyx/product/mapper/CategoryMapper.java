package cn.itedus.ssyx.product.mapper;

import cn.itedus.ssyx.model.product.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-12 16:02
 * @description: 商品类别仓储服务
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
