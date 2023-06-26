package cn.itedus.ssyx.activity.mapper;

import cn.itedus.ssyx.model.activity.ActivityInfo;
import cn.itedus.ssyx.model.activity.ActivityRule;
import cn.itedus.ssyx.model.activity.ActivitySku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-15 14:51
 * @description:
 */

@Mapper
public interface ActivityInfoMapper extends BaseMapper<ActivityInfo> {
    List<Long> selecExistIdList(@Param("skuIdList") List<Long> skuIdList);

    List<ActivityRule> selectActivityRuleList(@Param("skuId") Long skuId);

    List<ActivitySku> selectCartActivityList(@Param("skuIdList") List<Long> skuIdList);
}
