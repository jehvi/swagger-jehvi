package com.jehvi.swaggerdemo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * @author: lijh
 * @date: 2020/8/19
 * @package: com.jehvi.swaggerdemo.common
 * @description: 通用应答类 T 泛型标记可以指定返回的业务对象，传入构造器
 */
@Getter
@Setter
@ToString
@ApiModel(description = "通用应答返回信息")
@NoArgsConstructor
public class CommonResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_SUCCESS_MESSAGE = "操作成功";
    private static final String DEFAULT_FAIL_MESSAGE = "操作失败";
    private static final String DEFAULT_NULL_MESSAGE = "暂无承载数据";

    @ApiModelProperty(value = "状态码", required = true)
    private int code;
    @ApiModelProperty(value = "是否成功", required = true)
    private boolean success;
    @ApiModelProperty(value = "承载数据")
    private T data;
    @ApiModelProperty(value = "返回消息", required = true)
    private String msg;

    /**
     * 无承载数据对象的构造 根据code返回msg
     * @param resultCode
     */
    private CommonResult(IResultCode resultCode) {
        this(resultCode, null, resultCode.getMessage());
    }

    /**
     * 无承载数据对象的构造 传入返回msg
     * @param resultCode
     * @param msg
     */
    private CommonResult(IResultCode resultCode, String msg) {
        this(resultCode, null, msg);
    }

    /**
     * 带承载数据对象的返回 json
     * @param resultCode
     * @param data
     */
    private CommonResult(IResultCode resultCode, T data) {
        this(resultCode, data, resultCode.getMessage());
    }

    /**
     * 带承载数据对象的返回 json 自定义返回 msg
     * @param resultCode
     * @param data
     * @param msg
     */
    private CommonResult(IResultCode resultCode, T data, String msg) {
        this(resultCode.getCode(), data, msg);
    }

    /**
     * 参数构造
     * @param code
     * @param data
     * @param msg
     */
    private CommonResult(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.success = ResultCode.SUCCESS.code == code;
    }

    /**
     * 下述封装了统一的返回码、信息与对象方法 根据不同场景选择使用
     */

    /**
     * 返回承载对象与消息 默认成功 HTTP code码
     *
     * @param <T>  T 泛型标记
     * @param data 数据
     * @return CommonResult
     */
    public static <T> CommonResult<T> data(T data) {
        return data(data, DEFAULT_SUCCESS_MESSAGE);
    }

    /**
     * 返回承载对象 自动以成功消息
     *
     * @param data 数据
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return CommonResult
     */
    public static <T> CommonResult<T> data(T data, String msg) {
        return data(HttpServletResponse.SC_OK, data, msg);
    }

    /**
     * 返回
     *
     * @param code 状态码
     * @param data 数据
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return CommonResult
     */
    public static <T> CommonResult<T> data(int code, T data, String msg) {
        return new CommonResult<>(code, data, data == null ? DEFAULT_NULL_MESSAGE : msg);
    }

    /**
     * 返回成功
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return CommonResult
     */
    public static <T> CommonResult<T> success(String msg) {
        return new CommonResult<>(ResultCode.SUCCESS, msg);
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param <T>        T 泛型标记
     * @return CommonResult
     */
    public static <T> CommonResult<T> success(IResultCode resultCode) {
        return new CommonResult<>(resultCode);
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param msg        消息
     * @param <T>        T 泛型标记
     * @return CommonResult
     */
    public static <T> CommonResult<T> success(IResultCode resultCode, String msg) {
        return new CommonResult<>(resultCode, msg);
    }

    /**
     * 返回CommonResult
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return CommonResult
     */
    public static <T> CommonResult<T> fail(String msg) {
        return new CommonResult<>(ResultCode.FAILURE, msg);
    }


    /**
     * 返回R
     *
     * @param code 状态码
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return CommonResult
     */
    public static <T> CommonResult<T> fail(int code, String msg) {
        return new CommonResult<>(code, null, msg);
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param <T>        T 泛型标记
     * @return CommonResult
     */
    public static <T> CommonResult<T> fail(IResultCode resultCode) {
        return new CommonResult<>(resultCode);
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param msg        消息
     * @param <T>        T 泛型标记
     * @return CommonResult
     */
    public static <T> CommonResult<T> fail(IResultCode resultCode, String msg) {
        return new CommonResult<>(resultCode, msg);
    }

    /**
     * 返回R
     *
     * @param flag 成功状态
     * @return CommonResult
     */
    public static <T> CommonResult<T> status(boolean flag) {
        return flag ? success(DEFAULT_SUCCESS_MESSAGE) : fail(DEFAULT_FAIL_MESSAGE);
    }

}
