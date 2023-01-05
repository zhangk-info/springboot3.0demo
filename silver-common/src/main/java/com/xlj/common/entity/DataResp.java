package com.xlj.common.entity;

import cn.hutool.json.JSONObject;
import com.xlj.common.exception.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;

/**
 * @author zhangkun
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DataResp<T> extends JSONObject {
    /**
     * 系统警告消息
     */
    public static final int WARN = 601;
    private T data;
    private int code = 0;
    private String msg = "success.";

    public DataResp(int code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
        super.set("code", code);
        super.set("msg", message);
        super.set("data", data);
    }

    public DataResp(T data) {
        this.data = data;
    }

    public static <T> DataResp<T> success() {
        return new DataResp<T>(0, null, null);
    }

    public static <T> DataResp<T> success(String message) {
        return new DataResp<T>(0, message, null);
    }

    public static <T> DataResp<T> success(T data) {
        return new DataResp<T>(0, "success.", data);
    }

    public static <T> DataResp<T> success(T data, String message) {
        return new DataResp<T>(0, message, data);
    }

    public static <T> DataResp<T> error(T data, String message) {
        return new DataResp<T>(ErrorCode.DEFAULT.code, message, data);
    }

    public static <T> DataResp<T> error(T data, String message, int code) {
        return new DataResp<T>(code, message, data);
    }

    public static <T> DataResp<T> error() {
        return new DataResp<T>(ErrorCode.DEFAULT.code, null, null);
    }

    public static <T> DataResp<T> error(String message) {
        return new DataResp<T>(ErrorCode.DEFAULT.code, message, null);
    }

    public static <T> DataResp<T> error(String message, int code) {
        return new DataResp<T>(code, message, null);
    }

    public static <T> DataResp<T> warn(T data, String message) {
        return new DataResp<T>(WARN, message, data);
    }

    public static <T> DataResp<T> warn(T data, String message, int code) {
        return new DataResp<T>(code, message, data);
    }

    public static <T> DataResp<T> warn() {
        return new DataResp<T>(WARN, null, null);
    }

    public static <T> DataResp<T> warn(String message) {
        return new DataResp<T>(WARN, message, null);
    }

    public static <T> DataResp<T> warn(String message, int code) {
        return new DataResp<T>(code, message, null);
    }

}
