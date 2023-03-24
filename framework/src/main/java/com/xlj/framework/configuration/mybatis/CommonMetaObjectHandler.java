package com.xlj.framework.configuration.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xlj.common.context.UserContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * mybatis-plus自动填充配置
 *
 * @author zhangkun
 */
@Component
public class CommonMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createAt", LocalDateTime.now(), metaObject);
        this.setFieldValByName("createBy", UserContext.getId(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateAt", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateBy", getCurrentUser(), metaObject);
        Boolean isDelete = (Boolean) this.getFieldValByName("isDelete", metaObject);
        if (Objects.nonNull(isDelete) && isDelete) {
            this.setFieldValByName("deleteAt", LocalDateTime.now(), metaObject);
            this.setFieldValByName("deleteBy", getCurrentUser(), metaObject);
        }
    }

    private String getCurrentUser() {
        return String.format("%s(%s)", UserContext.getUsername(), UserContext.getId());
    }

}
