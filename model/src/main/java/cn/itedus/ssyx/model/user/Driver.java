package cn.itedus.ssyx.model.user;


import cn.itedus.ssyx.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Driver")
@TableName("driver")
public class Driver extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "名称")
	@TableField("name")
	private String name;

	@ApiModelProperty(value = "手机")
	@TableField("phone")
	private String phone;

	@ApiModelProperty(value = "仓库id")
	@TableField("ware_id")
	private Long wareId;

}