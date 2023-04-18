package com.xlj.common.entity;

import com.xlj.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangkun
 */
@Getter
@Setter
@Schema(name = "DataResp", description = "请求数据返回消息体")
public class DataResp<T> {
    /**
     * 系统警告消息
     */
    public static final int WARN = 601;
    @Schema(description = "数据")
    private T data;
    @Schema(description = "状态码 0 成功 其他失败")
    private int code = 0;
    @Schema(description = "返回消息")
    private String msg = "success.";

    public DataResp(Integer code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    public static <T> DataResp<T> success() {
        return new DataResp<>(0, null, null);
    }

    public static <T> DataResp<T> successMsg(String message) {
        return new DataResp<>(0, message, null);
    }

    public static <T> DataResp<T> success(T data) {
        return new DataResp<>(0, "success.", data);
    }

    public static <T> DataResp<T> success(T data, String message) {
        return new DataResp<>(0, message, data);
    }

    public static <T> DataResp<T> error(T data, String message) {
        return new DataResp<>(ErrorCode.DEFAULT.code, message, data);
    }

    public static <T> DataResp<T> error(T data, String message, int code) {
        return new DataResp<>(code, message, data);
    }

    public static <T> DataResp<T> error() {
        return new DataResp<>(ErrorCode.DEFAULT.code, null, null);
    }

    public static <T> DataResp<T> error(String message) {
        return new DataResp<>(ErrorCode.DEFAULT.code, message, null);
    }

    public static <T> DataResp<T> error(Integer code, String message) {
        return new DataResp<>(code, message, null);
    }

    public static <T> DataResp<T> warn(T data, String message) {
        return new DataResp<>(WARN, message, data);
    }

    public static <T> DataResp<T> warn(T data, String message, int code) {
        return new DataResp<>(code, message, data);
    }

    public static <T> DataResp<T> warn() {
        return new DataResp<>(WARN, null, null);
    }

    public static <T> DataResp<T> warn(String message) {
        return new DataResp<>(WARN, message, null);
    }

    public static <T> DataResp<T> warn(String message, int code) {
        return new DataResp<>(code, message, null);
    }

}
