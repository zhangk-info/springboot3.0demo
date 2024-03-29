package com.xlj.framework.filter.web_security;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xlj.common.exception.ServiceException;
import com.xlj.common.utils.HttpHelper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.DelegatingServletInputStream;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    /**
     * html过滤
     */
    private static final HTMLFilter HTML_FILTER = new HTMLFilter();
    /**
     * 没被包装过的HttpServletRequest（特殊场景，需要自己过滤）
     */
    private final HttpServletRequest orgRequest;

    private final byte[] body;
    private JSONObject jsonObject;

    public XssHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        orgRequest = request;
        this.body = HttpHelper.getBodyString(request).getBytes(StandardCharsets.UTF_8);
        this.getInputStream();
    }

    /**
     * 获取最原始的request
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest request) {
        if (request instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) request).getOrgRequest();
        }

        return request;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        // 非json类型，直接返回
        if (!StringUtils.startsWithIgnoreCase(super.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
            return super.getInputStream();
        }

        // 为空，直接返回
        String json = new String(body, StandardCharsets.UTF_8);
        if (StringUtils.isBlank(json)) {
            return super.getInputStream();
        } else {
            if (JSONUtil.isTypeJSON(json)) {
                jsonObject = JSONUtil.parseObj(json);
            }
        }

        //xss过滤
        json = xssEncode(json);
        final ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        return new DelegatingServletInputStream(bis);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(xssEncode(name));
        if (StringUtils.isNotBlank(value)) {
            sqlInject(name, value);
            value = xssEncode(value);
        }
        if (Objects.isNull(value) && Objects.nonNull(jsonObject) && jsonObject.containsKey(name)) {
            return JSONUtil.toJsonStr(jsonObject.get(name));
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameters = super.getParameterValues(name);
        if (parameters == null || parameters.length == 0) {
            return null;
        }

        for (int i = 0; i < parameters.length; i++) {
            sqlInject(name, parameters[i]);
            parameters[i] = xssEncode(parameters[i]);
        }
        return parameters;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = new LinkedHashMap<>();
        Map<String, String[]> parameters = super.getParameterMap();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            for (int i = 0; i < values.length; i++) {
                sqlInject(key, values[i]);
                values[i] = xssEncode(values[i]);
            }
            map.put(key, values);
        }
        return map;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(xssEncode(name));
        if (StringUtils.isNotBlank(value)) {
            sqlInject(name, value);
            value = xssEncode(value);
        }
        return value;
    }

    private String xssEncode(String input) {
        return HTML_FILTER.filter(input);
    }

    public void sqlInject(String name, String value) {
        if (StringUtils.isBlank(value)) {
            return;
        }
        if ("sortOrder".equals(name)) {
            if (!"desc".equalsIgnoreCase(value) && !"asc".equalsIgnoreCase(value)) {
                throw new ServiceException("参数错误：排序方式只能是正序或者倒序");
            }
        }
        if ("sortName".equals(name) || "orderByColumn".equals(name)) {
            SQLInjectShield.sqlValidate(value);
        }
    }

    /**
     * 获取最原始的request
     */
    private HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

}