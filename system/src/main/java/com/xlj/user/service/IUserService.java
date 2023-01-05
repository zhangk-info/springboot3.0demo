package com.xlj.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xlj.user.entity.User;
import com.xlj.user.request.UserDTO;
import com.xlj.user.request.UserQueryDTO;
import com.xlj.user.response.UserVO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zhangkun
 * @since 2022-12-29
 */
public interface IUserService extends IService<User> {

    /**
     * 分页列表
     *
     * @param queryDTO 查询条件
     * @return 分页列表
     */
    IPage<UserVO> findByPage(UserQueryDTO queryDTO);

    /**
     * 新增或修改
     *
     * @param userDTO 请求体对象
     * @return 实体对象
     */
    UserVO createOrUpdate(UserDTO userDTO);

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
