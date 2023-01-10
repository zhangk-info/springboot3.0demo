package com.xlj.common.entity;

import cn.hutool.core.util.StrUtil;
import com.xlj.common.utils.SqlUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhangkun
 */
@Schema(name = "基础查询条件")
public class BaseQuery {

    @Schema(description = "页码，默认1")
    private Integer pageNum = 1;
    @Schema(description = "每页行数，默认20")
    private Integer pageSize = 20;
    @Schema(description = "排序字段")
    private String sortName = "t.id";
    @Schema(description = "排序方式 ASC 升序 DESC 降序")
    private String sortOrder = "DESC";

    @Schema(description = "数据权限")
    private String dataScope = "";

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public Integer getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortName() {
        //大写转下划线
        return SqlUtils.camelToUnderline(StringUtils.isNotBlank(sortName) ? this.sortName : "t.id");
    }

    public void setSortName(String sortName) {
        this.sortName = SqlUtils.camelToUnderline(sortName);
    }

    public String getSortOrder() {
        return this.sortOrderLegal() ? this.sortOrder : "desc";
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    private boolean sortOrderLegal() {
        return StrUtil.equalsAnyIgnoreCase("desc", this.sortOrder) || StrUtil.equalsAnyIgnoreCase("asc", this.sortOrder);
    }
}
