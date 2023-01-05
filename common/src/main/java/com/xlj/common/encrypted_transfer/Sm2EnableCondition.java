package com.xlj.common.encrypted_transfer;

import cn.hutool.core.util.StrUtil;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author zhangkun
 */
public class Sm2EnableCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        String property = environment.getProperty("sm2.enable");
        if (StrUtil.isNotEmpty(property) && property.equals("true")) {
            return true;
        }

        return false;
    }
}
