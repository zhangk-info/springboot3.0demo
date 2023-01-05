package com.xlj.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xlj.common.entity.DataResp;
import com.xlj.user.request.UserDTO;
import com.xlj.user.request.UserQueryDTO;
import com.xlj.user.response.UserVO;
import com.xlj.user.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zhangkun
 * @since 2022-12-29
 */
@RestController
@RequestMapping("/users")
@Tag(name = "用户表")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 分页列表
     *
     * @param queryDTO 查询条件
     * @return 分页列表
     */
    @GetMapping
    @Operation(summary = "分页列表", description = "分页列表")
    DataResp<IPage<UserVO>> page(UserQueryDTO queryDTO) {
        return DataResp.success(userService.findByPage(queryDTO));
    }

    /**
     * 新增或修改
     *
     * @param userDTO 请求体对象
     * @return 实体对象
     */
    @PostMapping
    @Operation(summary = "新增或修改", description = "新增或修改")
    DataResp<UserVO> createOrUpdate(@RequestBody @Valid UserDTO userDTO) {
        return DataResp.success(userService.createOrUpdate(userDTO));
    }

    /**
     * 删除
     *
     * @param id 删除的ID
     * @return 成功状态
     */
    @DeleteMapping("{id}")
    @Operation(summary = "删除", description = "删除")
    DataResp<Boolean> save(@PathVariable Long id) {
        return DataResp.success(userService.delete(id));
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
        return DataResp.success(userService.check(id, field, value));
    }
}
