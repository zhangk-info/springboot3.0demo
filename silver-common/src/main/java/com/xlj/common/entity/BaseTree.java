package com.xlj.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BaseTree<T> implements Serializable {
    private static final long serialVersionUID = -8509801876544848046L;
    private Long id;
    private Long pid;
    private Integer level;
    private Boolean hasChild;
    private List<T> children;
}
