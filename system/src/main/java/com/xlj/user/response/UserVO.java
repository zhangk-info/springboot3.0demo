package com.xlj.user.response;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
@Schema(name = "UserVO", description = "用户表VO对象")
@ExcelIgnoreUnannotated
public class UserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "微信openid")
    @ExcelProperty("微信openid")
    private String openid;

    @Schema(description = "电话号码")
    @ExcelProperty("电话号码")
    private String mobile;

    @Schema(description = "密码")
    @JsonIgnore
    private String password;

    @Schema(description = "用户名")
    @ExcelProperty("用户名")
    private String username;

    @Schema(description = "昵称")
    @ExcelProperty("昵称")
    private String nickname;

    @Schema(description = "头像")
    private String coverUrl;

    @Schema(description = "店铺名称")
    @ExcelProperty("店铺名称")
    private String companyName;

    @Schema(description = "省份ID")
    private Integer provinceId;

    @Schema(description = "省份名称")
    @ExcelProperty("省份名称")
    private String province;

    @Schema(description = "城市ID")
    private Integer cityId;

    @Schema(description = "城市名称")
    @ExcelProperty("城市名称")
    private String city;

    @Schema(description = "县区ID")
    private Integer districtId;

    @Schema(description = "县区名称")
    @ExcelProperty("县区名称")
    private String district;

    @Schema(description = "登录后默认选择的store_id")
    private Integer lastStoreId;

    @Schema(description = "预存保险费")
    @ExcelProperty("预存保险费")
    private BigDecimal balance;

    @Schema(description = "金店店铺详细地址")
    @ExcelProperty("金店店铺详细地址")
    private String address;

    @Schema(description = "创建时间")
    private Date createAt;

    @Schema(description = "更新时间")
    private Date updateAt;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "更新人")
    private String updateBy;
}
