package cn.itedus.ssyx.vo.sys;

import lombok.Data;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;

@Data
public class RegionWareQueryVo {
	
	@ApiModelProperty(value = "关键字")
	private String keyword;

}

