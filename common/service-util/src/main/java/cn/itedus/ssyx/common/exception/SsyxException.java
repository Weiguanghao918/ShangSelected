package cn.itedus.ssyx.common.exception;

import cn.itedus.ssyx.common.result.ResultCodeEnum;
import lombok.Data;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-08 15:07
 * @description: 自定义异常类
 */
@Data
public class SsyxException extends RuntimeException {

    //异常类状态码
    private Integer code;

    /**
     * 通过状态码和错误消息创建异常
     *
     * @param message 错误消息
     * @param code    状态码
     */
    public SsyxException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 通过枚举类创建异常
     *
     * @param resultCodeEnum 枚举类
     */
    public SsyxException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "SsyxException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }

}
