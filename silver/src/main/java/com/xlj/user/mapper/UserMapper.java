package com.xlj.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xlj.user.entity.User;
import com.xlj.user.request.UserQueryDTO;
import com.xlj.user.response.UserVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author zhangkun
 * @since 2022-12-29
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 分页列表
     *
     * @param page     分页
     * @param queryDTO 查询条件
     * @return 分页列表
     */
    IPage<UserVO> findByPage(@Param("page") IPage page, @Param("query") UserQueryDTO queryDTO);
}
