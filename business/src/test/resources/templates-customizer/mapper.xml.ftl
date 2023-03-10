<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

<#if enableCache>
    <!-- 开启二级缓存 -->
    <cache type="${cacheClassName}"/>

</#if>
<#if baseResultMap>
    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
<#list table.fields as field>
<#if field.keyFlag><#--生成主键排在第一位-->
        <id column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
<#list table.commonFields as field><#--生成公共字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#list>
<#list table.fields as field>
<#if !field.keyFlag><#--生成普通字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
    </resultMap>

</#if>
<#if baseColumnList>
    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
<#list table.commonFields as field>
        ${field.columnName},
</#list>
        ${table.fieldNames}
    </sql>

</#if>

    <!-- 分页列表 -->
    <select id="findByPage" resultType="${package.Parent}.response.${entity}VO">
        select * from ${table.name} t
        <where>
            t.is_delete = 0
            <if test="query.keyWord != null and query.keyWord != ''">
                ${r"<!--and (t.x like concat('%', #{query.keyWord} ,'%') or t.x like concat('%',#{query.keyWord},'%'))-->"}
            </if>
        </where>
        <if test="query.sortName != null and query.sortName != null and query.sortName != '' and query.sortName != ''">
            ${r"order by ${query.sortName} ${query.sortOrder}"}
        </if>
    </select>
</mapper>
