package com.xlj.framework.configuration.auth.controller;

import com.xlj.common.context.UserContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    @RequestMapping("/logout")
    public void logout() {
        UserContext.getId();
    }
}
