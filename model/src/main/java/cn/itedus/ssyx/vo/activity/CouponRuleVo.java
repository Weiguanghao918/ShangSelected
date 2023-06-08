package cn.itedus.ssyx.vo.activity;


import cn.itedus.ssyx.enums.CouponRangeType;
import cn.itedus.ssyx.model.activity.CouponRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(description = "优惠券规则")
public class CouponRuleVo implements Serializable {
   
   private static final long serialVersionUID = 1L;

   @ApiModelProperty(value = "优惠券id")
   private Long couponId;

   @ApiModelProperty(value = "范围类型")
   private CouponRangeType rangeType;

   @ApiModelProperty(value = "金额")
   private BigDecimal amount;

   @ApiModelProperty(value = "使用门槛 0->没门槛")
   private BigDecimal conditionAmount;

   @ApiModelProperty(value = "优惠券参与的商品list")
   private List<CouponRange> couponRangeList;

   @ApiModelProperty(value = "优惠券范围描述")
   private String rangeDesc;

}