package cn.itedus.ssyx.common.exception;

import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.common.result.ResultCodeEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-08 15:10
 * @description: 全局异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail(null, ResultCodeEnum.PAYMENT_ERROR);
    }

    /**
     * 自定义异常处理方法
     *
     * @param e 异常数据
     * @return 结果集
     */
    @ExceptionHandler(SsyxException.class)
    @ResponseBody
    public Result error(SsyxException e) {
        return Result.build(null, e.getCode(), e.getMessage());
    }
}
