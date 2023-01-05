package com.xlj.framework.configuration.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xlj.common.context.UserContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mybatis-plus自动填充配置
 *
 * @author zhangkun
 */
@Component
public class CommonMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("create_at", new Date(), metaObject);
        this.setFieldValByName("create_by", UserContext.getId(),metaObject);
        this.setFieldValByName("update_at", new Date(), metaObject);
        this.setFieldValByName("update_by", UserContext.getId(),metaObject);
        this.updateFill(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("update_at", new Date(), metaObject);
        this.setFieldValByName("update_by", UserContext.getId(),metaObject);
        Integer isDelete = (Integer) this.getFieldValByName("is_delete", metaObject);
        if (isDelete.equals(1)) {
            this.setFieldValByName("delete_at", new Date(), metaObject);
            this.setFieldValByName("delete_by", UserContext.getId(),metaObject);
        }
    }

    private String getCurrentUser() {
        return String.format("%s(%s)", UserContext.getUsername(), UserContext.getName());
    }

}
