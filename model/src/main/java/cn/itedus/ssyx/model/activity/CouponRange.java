package cn.itedus.ssyx.model.activity;


import cn.itedus.ssyx.enums.CouponRangeType;
import cn.itedus.ssyx.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * CouponRange
 * </p>
 *
 * @author qy
 */
@Data
@ApiModel(description = "CouponRange")
@TableName("coupon_range")
public class CouponRange extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "优惠券id")
	@TableField("coupon_id")
	private Long couponId;

	@ApiModelProperty(value = "范围类型 1、商品(spuid) 2、品类(三级分类id) 3、品牌")
	@TableField("range_type")
	private CouponRangeType rangeType;

	@ApiModelProperty(value = "rangeId")
	@TableField("range_id")
	private Long rangeId;

}

