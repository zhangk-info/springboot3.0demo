package com.xlj.system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页
 *
 * @author ruoyi
 */
@RestController
public class SysIndexController {
    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index() {
        return String.format("欢迎使用%s后台管理框架，当前版本：v%s，请通过前端地址访问。", "测试测试", "1.0");
    }
}
