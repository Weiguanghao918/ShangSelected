package cn.itedus.ssyx.common.result;

import lombok.Data;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-08 15:02
 * @description: 统一返回结果类
 */
@Data
public class Result<T> {

    //状态码
    private Integer code;
    //信息
    private String message;
    //数据
    private T data;

    //私有化构造器
    private Result() {
    }

    /**
     * 设置数据，返回对象的方法
     *
     * @param data           数据
     * @param resultCodeEnum 枚举类
     * @param <T>            泛型
     * @return 结果集
     */
    public static <T> Result<T> build(T data, ResultCodeEnum resultCodeEnum) {
        Result<T> result = new Result<>();
        if (null != data) {
            result.setData(data);
        }

        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    public static <T> Result<T> build(T data, int code, String message) {
        Result<T> result = new Result<>();
        if (null != data) {
            result.setData(data);
        }
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 成功的方法返回
     *
     * @param data 数据
     * @param <T>  泛型
     * @return 结果集
     */
    public static <T> Result<T> ok(T data) {
        Result<T> result = build(data, ResultCodeEnum.SUCCESS);
        return result;
    }

    public static <T> Result<T> ok() {
        Result<T> result = build(null, ResultCodeEnum.SUCCESS);
        return result;
    }



    /**
     * 失败的结果集返回
     *
     * @param <T>          泛型
     * @param data         数据
     * @param paymentError
     * @return 结果集
     */
    public static <T> Result<T> fail(T data, ResultCodeEnum paymentError) {
        return build(data, ResultCodeEnum.FAIL);
    }

    public static <T> Result<T> fail() {
        return build(null, ResultCodeEnum.FAIL);
    }



}
