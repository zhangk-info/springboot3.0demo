package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};
import com.baomidou.mybatisplus.core.metadata.IPage;
import ${package.Parent}.request.${entity}QueryDTO;
import ${package.Parent}.response.${entity}VO;
import org.apache.ibatis.annotations.Param;
<#if mapperAnnotationClass??>
import ${mapperAnnotationClass.name};
</#if>

/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if mapperAnnotationClass??>
@${mapperAnnotationClass.simpleName}
</#if>
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

    /**
     * 分页列表
     *
     * @param page     分页
     * @param queryDTO 查询条件
     * @return 分页列表
     */
    IPage<${entity}VO> findByPage(@Param("page") IPage<${entity}VO> page, @Param("query") ${entity}QueryDTO queryDTO);
}
</#if>
