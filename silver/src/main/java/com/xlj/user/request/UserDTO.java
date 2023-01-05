package com.xlj.user.request;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
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
@Schema(name = "UserDTO", description = "用户表新增&修改请求体")
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "微信openid")
    private String openid;

    @Schema(description = "电话号码")
    @NotNull
    private String mobile;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String coverUrl;

    @Schema(description = "店铺名称")
    private String companyName;

    @Schema(description = "省份ID")
    private Integer provinceId;

    @Schema(description = "省份名称")
    private String province;

    @Schema(description = "城市ID")
    private Integer cityId;

    @Schema(description = "城市名称")
    private String city;

    @Schema(description = "县区ID")
    private Integer districtId;

    @Schema(description = "县区名称")
    private String district;

    @Schema(description = "登录后默认选择的store_id")
    private Integer lastStoreId;

    @Schema(description = "预存保险费")
    private BigDecimal balance;

    @Schema(description = "金店店铺详细地址")
    private String address;
}
