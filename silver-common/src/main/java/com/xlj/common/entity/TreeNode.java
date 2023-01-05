package com.xlj.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangkun
 * @date 2017年11月9日23:33:45
 */
@Data
@Schema(name = "树形节点")
public class TreeNode<T> {

    @Schema(description = "当前节点id")
    protected String id;
    @Schema(description = "父节点id")
    protected String parentId;
    @Schema(description = "树状结构id，当层级结构不是同一对象，id会重复时，可使用此字段")
    protected Integer treeId;
    @Schema(description = "父级树状结构id")
    protected Integer parentTreeId;
    @Schema(description = "子节点列表")
    protected List<T> children = new ArrayList<>();
    @Schema(description = "当前节点等级")
    private Integer level;
    @Schema(description = "是否有字")
    private Boolean hasChild;
    @Schema(description = "上级节点ids")
    private String parentIds;

    public void addChild(T node) {
        children.add(node);
    }
}
