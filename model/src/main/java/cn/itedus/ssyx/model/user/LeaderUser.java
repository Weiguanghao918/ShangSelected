package cn.itedus.ssyx.model.user;


import cn.itedus.ssyx.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "LeaderUser")
@TableName("leader_user")
public class LeaderUser extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "团长ID")
	@TableField("leader_id")
	private String leaderId;

	@ApiModelProperty(value = "userId")
	@TableField("user_id")
	private Long userId;

}