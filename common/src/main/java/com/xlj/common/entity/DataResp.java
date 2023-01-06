package com.xlj.common.entity;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import com.xlj.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * @author zhangkun
 */
@Getter
@Setter
@Schema(name = "DataResp", description = "请求数据返回消息体")
public class DataResp<T> extends ResponseEntity<T> {
    /**
     * 系统警告消息
     */
    public static final int WARN = 601;
    @Schema(description = "数据")
    private T data;
    @Schema(description = "状态码 0 成功 其他失败", implementation = Object.class)
    private int code = 0;
    @Schema(description = "返回消息")
    private String msg = "success.";
    @Hidden
    private JSONConfig config;
    @Hidden
    private JSONObject dateFormat;
    @Hidden
    private Boolean empty;
    @Hidden
    private Map<String, Object> raw;

    public DataResp(int code, String message, T data) {
        super(HttpStatusCode.valueOf(200));
        this.code = code;
        this.msg = message;
        this.data = data;
//        super.set("code", code);
//        super.set("msg", message);
//        super.set("data", data);
    }

    public DataResp(T data) {
        super(HttpStatusCode.valueOf(200));
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
