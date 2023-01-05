package com.xlj.system.domain.entity;


import com.alibaba.excel.annotation.ExcelProperty;
import com.xlj.common.entity.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 岗位表 sys_post
 *
 * @author ruoyi
 */
public class SysPost extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 岗位序号
     */
    @ExcelProperty("岗位序号")
    private Long postId;

    /**
     * 岗位编码
     */
    @ExcelProperty("岗位编码")
    private String postCode;

    /**
     * 岗位名称
     */
    @ExcelProperty("岗位名称")
    private String postName;

    /**
     * 岗位排序
     */
    @ExcelProperty("岗位排序")
    private Integer postSort;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty("状态")
    private String status;

    /**
     * 用户是否存在此岗位标识 默认不存在
     */
    private boolean flag = false;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @NotBlank(message = "岗位编码不能为空")
    @Size(min = 0, max = 64, message = "岗位编码长度不能超过64个字符")
    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @NotBlank(message = "岗位名称不能为空")
    @Size(min = 0, max = 50, message = "岗位名称长度不能超过50个字符")
    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getPostSort() {
        return postSort;
    }

    public void setPostSort(Integer postSort) {
        this.postSort = postSort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "SysPost{" +
                "postId=" + postId +
                ", postCode='" + postCode + '\'' +
                ", postName='" + postName + '\'' +
                ", postSort=" + postSort +
                ", status='" + status + '\'' +
                ", flag=" + flag +
                "} " + super.toString();
    }
}
