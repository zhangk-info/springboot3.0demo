package com.xlj.user.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xlj.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.math.BigDecimal;

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
@TableName("users")
@Schema(name = "User", description = "用户表")
public class User extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "微信openid")
    private String openid;

    private String mobile;

    private String password;

    private String username;

    private String nickname;

    private String coverUrl;

    private String companyName;

    private Integer provinceId;

    @Schema(description = "省份名称")
    private String province;

    private Integer cityId;

    @Schema(description = "城市名称")
    private String city;

    private Integer districtId;

    @Schema(description = "县区名称")
    private String district;

    @TableLogic
    private Boolean isDelete;

    @Schema(description = "登录后默认选择的store_id")
    private Integer lastStoreId;

    @Schema(description = "预存保险费")
    private BigDecimal balance;

    @Schema(description = "金店店铺详细地址")
    private String address;
}
