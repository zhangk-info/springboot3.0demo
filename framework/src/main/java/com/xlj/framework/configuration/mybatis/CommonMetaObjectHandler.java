package com.xlj.framework.configuration.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xlj.common.context.UserContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

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
        this.setFieldValByName("createAt", new Date(), metaObject);
        this.setFieldValByName("createBy", UserContext.getId(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateAt", new Date(), metaObject);
        this.setFieldValByName("updateBy", UserContext.getId(), metaObject);
        Integer isDelete = (Integer) this.getFieldValByName("isDelete", metaObject);
        if (Objects.nonNull(isDelete) && isDelete.equals(1)) {
            this.setFieldValByName("deleteAt", new Date(), metaObject);
            this.setFieldValByName("deleteBy", UserContext.getId(), metaObject);
        }
    }

    private String getCurrentUser() {
        return String.format("%s(%s)", UserContext.getUsername(), UserContext.getName());
    }

}
