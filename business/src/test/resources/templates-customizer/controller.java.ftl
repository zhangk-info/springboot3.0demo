package ${package.Controller};

<#if springdoc>
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
</#if>
import com.baomidou.mybatisplus.core.metadata.IPage;
import ${package.Parent}.request.${entity}DTO;
import ${package.Parent}.request.${entity}QueryDTO;
import ${package.Parent}.response.${entity}VO;
import ${package.Parent}.service.I${entity}Service;
import com.xlj.common.entity.DataResp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>s")
<#if springdoc>
@Tag(name = "${table.comment!}")
</#if>
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    @Autowired
    private I${entity}Service ${entity?uncap_first}Service;

    /**
     * 分页列表
     *
     * @param queryDTO 查询条件
     * @return 分页列表
     */
    @GetMapping
    @Operation(summary = "分页列表", description = "分页列表")
    DataResp<IPage<${entity}VO>> page(${entity}QueryDTO queryDTO) {
        return DataResp.success(${entity?uncap_first}Service.findByPage(queryDTO));
    }

    /**
     * 新增或修改
     *
     * @param ${entity?uncap_first}DTO 请求体对象
     * @return 实体对象
     */
    @PostMapping
    @Operation(summary = "新增或修改", description = "新增或修改")
    DataResp<${entity}VO> createOrUpdate(@RequestBody @Valid ${entity}DTO ${entity?uncap_first}DTO) {
        return DataResp.success(${entity?uncap_first}Service.createOrUpdate(${entity?uncap_first}DTO));
    }

    /**
     * 删除
     *
     * @param ids 删除的ID
     * @return 成功状态
     */
    @DeleteMapping("{ids}")
    @Operation(summary = "删除", description = "删除")
    DataResp<Boolean> batchDel(@PathVariable Long[] ids) {
        return DataResp.success(${entity?uncap_first}Service.delete(ids));
    }

    /**
     * 详情
     *
     * @param id ID
     * @return 实体对象
     */
    @GetMapping("{id}")
    @Operation(summary = "详情", description = "详情")
    DataResp<${entity}VO> getVo(@PathVariable Long id) {
        return DataResp.success(${entity?uncap_first}Service.getVoById(id));
    }

    /**
     * 重复检查
     *
     * @param id    ID
     * @param field 检查字段
     * @param value 检查的值
     * @return 是否重复
     */
    @GetMapping("check")
    @Operation(summary = "重复检查", description = "重复检查")
    DataResp<Boolean> check(@Parameter(description = "编辑时的主键") Long id,
                            @Parameter(description = "查询重复的字段") String field,
                            @Parameter(description = "值") String value) {
        return DataResp.success(${entity?uncap_first}Service.check(id, field, value));
    }
}
</#if>
