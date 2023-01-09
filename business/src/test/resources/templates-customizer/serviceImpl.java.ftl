package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlj.common.utils.SqlUtils;
import ${package.Parent}.request.${entity}DTO;
import ${package.Parent}.request.${entity}QueryDTO;
import ${package.Parent}.response.${entity}VO;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    /**
     * 分页列表
     *
     * @param queryDTO 查询条件
     * @return 分页列表
     */
    @Override
    public IPage<${entity}VO> findByPage(${entity}QueryDTO queryDTO) {
        IPage<${entity}VO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        return baseMapper.findByPage(page, queryDTO);
    }

    /**
     * 新增或修改
     *
     * @param ${entity?uncap_first}DTO 请求体对象
     * @return 实体对象
     */
    @Override
    public ${entity}VO createOrUpdate(${entity}DTO ${entity?uncap_first}DTO) {
        ${entity} ${entity};
        if (Objects.isNull(${entity?uncap_first}DTO.getId())) {
            ${entity} = new ${entity}();
        } else {
            ${entity} = this.getById(${entity?uncap_first}DTO.getId());
        }
        // 复制
        BeanUtil.copyProperties(${entity?uncap_first}DTO, ${entity});
        // 保存或更新
        this.saveOrUpdate(${entity});
        // 转换为VO并返回
        return convertVo(${entity});
    }

    /**
     * 删除
     *
     * @param ids 删除的ID
     * @return 成功状态
     */
    @Override
    public Boolean delete(Long[] ids) {
        return this.removeBatchByIds(Arrays.asList(ids));
    }

    /**
     * 重复检查
     *
     * @param id    ID
     * @param field 检查字段
     * @param value 检查的值
     * @return 是否重复
     */
    @Override
    public Boolean check(Long id, String field, String value) {
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(id)) {
            queryWrapper.ne("id", id);
        }
        queryWrapper.eq(SqlUtils.camelToUnderline(field), value);
        return this.count(queryWrapper) == 0;
    }

    /**
     * 详情
     * @param id ID
     * @return 实体对象
     */
    @Override
    public ${entity}VO getVoById(Long id) {
        return convertVo(getById(id));
    }

    /**
     * 类型转换成vo
     *
     * @param ${entity?uncap_first} 转换前entity
     * @return 转换后vo
     */
    private ${entity}VO convertVo(${entity} ${entity?uncap_first}) {
        ${entity}VO vo = new ${entity}VO();
        BeanUtil.copyProperties(${entity?uncap_first}, vo);
        return vo;
    }
}
</#if>
