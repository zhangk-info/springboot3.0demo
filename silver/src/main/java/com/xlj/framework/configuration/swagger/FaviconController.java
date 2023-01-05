package com.xlj.framework.configuration.swagger;

import cn.hutool.core.io.resource.ResourceUtil;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 有的网站需要一个页签图片
 *
 * @author zhangkun
 */
@RestController
@Hidden
public class FaviconController {

    @GetMapping("/favicon.ico")
    public void getFavicon(HttpServletResponse response) {
        try {
            response.getOutputStream().write(ResourceUtil.getStream("favicon.ico").readAllBytes());
        } catch (Exception e) {

        }
    }
}
