package com.xlj.framework.configuration.auth;

import com.xlj.common.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义权限认证注解，使用方式：
 *
 * @PreAuthorize("@pm.check('test')")
 * @PreAuthorize("hasAuthority('USER') or hasAuthority('message.read') ")
 */
@Service("pm")
@Slf4j
public class PermissionCheckService {

    public boolean check(String permission) {
        Long userId = UserContext.getId();

        Map<String, String> userPermissions = new HashMap<>();
        // 通过用户id从其他地方拿认证
        userPermissions.put("test", "test");
        return userPermissions.containsKey(permission);
    }
}