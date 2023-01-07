package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.baomidou.mybatisplus.core.metadata.IPage;
import ${package.Parent}.request.${entity}DTO;
import ${package.Parent}.request.${entity}QueryDTO;
import ${package.Parent}.response.${entity}VO;

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    /**
     * 分页列表
     *
     * @param queryDTO 查询条件
     * @return 分页列表
     */
    IPage<${entity}VO> findByPage(${entity}QueryDTO queryDTO);

    /**
     * 新增或修改
     *
     * @param ${entity?uncap_first}DTO 请求体对象
     * @return 实体对象
     */
    ${entity}VO createOrUpdate(${entity}DTO ${entity?uncap_first}DTO);

    /**
     * 删除
     *
     * @param id 删除的ID
     * @return 成功状态
     */
    Boolean delete(Long id);

    /**
     * 重复检查
     *
     * @param id    ID
     * @param field 检查字段
     * @param value 检查的值
     * @return 是否重复
     */
    Boolean check(Long id, String field, String value);
}
</#if>
