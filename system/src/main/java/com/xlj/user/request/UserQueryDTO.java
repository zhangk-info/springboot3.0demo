package com.xlj.user.request;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xlj.common.entity.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author zhangkun
 * @since 2022-12-29
 */
@Getter
@Setter
@ToString
@TableName("users")
@Schema(name = "UserQueryDTO", description = "用户表查询条件")
public class UserQueryDTO extends BaseQuery {

    @Schema(description = "关键字")
    private String keyWord;
}
