package com.jehvi.swaggerdemo.common;

/**
 * 业务代码接口
 * @author lijh
 */
public interface IResultCode {
    /**
     * 获取消息
     *
     * @return
     */
    String getMessage();

    /**
     * 获取状态码
     *
     * @return
     */
    int getCode();
}
