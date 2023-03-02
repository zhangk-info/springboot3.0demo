package com.xlj.common;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @version 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ControllerTest {
    @Autowired
    protected MockMvc mvc;

    public JSONObject execute(RequestTypeEnum type, String url, HttpHeaders headers, Object obj) throws Exception {
        Map<String, String> ps = new HashMap<>();
        if (obj != null) {
            JSONObject jsonObject = JSONUtil.parseObj(JSONUtil.toJsonStr(obj));
            for (String key : jsonObject.keySet()) {
                ps.put(key, jsonObject.getStr(key));
            }
        }
        return execute(type, url, headers, ps);
    }

    public JSONObject execute(RequestTypeEnum type, String url, HttpHeaders headers, Map<String, String> params) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = null;
        switch (type) {
            case GET:
                requestBuilder = MockMvcRequestBuilders.get(url);
                break;
            case POST:
                requestBuilder = MockMvcRequestBuilders.post(url);
                break;
            case DELETE:
                requestBuilder = MockMvcRequestBuilders.delete(url);
                break;
            case PUT:
                requestBuilder = MockMvcRequestBuilders.put(url);
                break;
        }
        requestBuilder.headers(headers);
        requestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                requestBuilder.param(entry.getKey(), entry.getValue() == null ? "" : entry.getValue());
            }
        }

        requestBuilder.content(JSONUtil.toJsonStr(params));
        requestBuilder.accept(MediaType.APPLICATION_JSON_VALUE);

        ResultActions result = mvc.perform(requestBuilder);


        MvcResult mvcResult = result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();// 返回执行请求的结果

        System.out.println(mvcResult.getResponse().getContentAsString());
        return JSONUtil.parseObj(mvcResult.getResponse().getContentAsString());
    }

}
