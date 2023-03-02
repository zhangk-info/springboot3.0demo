package com.xlj.login;

import cn.hutool.json.JSONObject;
import com.xlj.DemoApplication;
import com.xlj.common.ControllerTest;
import com.xlj.common.RequestTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * 评论测试
 */
@ContextConfiguration(classes = DemoApplication.class)
@Slf4j
public class LoginTest extends ControllerTest {

    private final String URI = "/";

    @Test
    public void testLogin() throws Exception {
        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Basic emhhbmdrOnpoYW5nay4uMTIz");
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("username", "13628344382");
        params.put("password", "b74e65bf3a02dcfb66d6e75316eb86f2");
        params.put("client_id", "password");
        params.put("client_secret", "xinglianjing");

        JSONObject jsonObject = this.execute(RequestTypeEnum.POST, URI + "oauth2/token", headers, params);
        String token = jsonObject.getStr("access_token");
        log.info(token);
    }
}