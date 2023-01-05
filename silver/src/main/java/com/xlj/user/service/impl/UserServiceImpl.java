package com.xlj.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlj.common.utils.SqlUtils;
import com.xlj.user.entity.User;
import com.xlj.user.mapper.UserMapper;
import com.xlj.user.request.UserDTO;
import com.xlj.user.request.UserQueryDTO;
import com.xlj.user.response.UserVO;
import com.xlj.user.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zhangkun
 * @since 2022-12-29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    /**
     * 分页列表
     *
     * @param queryDTO 查询条件
     * @return 分页列表
     */
    @Override
    public IPage<UserVO> findByPage(UserQueryDTO queryDTO) {
        IPage<UserVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        return baseMapper.findByPage(page, queryDTO);
    }

    /**
     * 新增或修改
     *
     * @param userDTO 请求体对象
     * @return 实体对象
     */
    @Override
    public UserVO createOrUpdate(UserDTO userDTO) {
        User user;
        if (Objects.isNull(userDTO.getId())) {
            user = new User();
        } else {
            user = this.getById(userDTO.getId());
        }
        // 复制
        BeanUtil.copyProperties(userDTO, user);
        // 保存或更新
        this.saveOrUpdate(user);
        // 转换为VO并返回
        return convertVo(user);
    }

    /**
     * 删除
     *
     * @param id 删除的ID
     * @return 成功状态
     */
    @Override
    public Boolean delete(Long id) {
        return this.removeById(id);
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
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(id)) {
            queryWrapper.ne("id", id);
        }
        queryWrapper.eq(SqlUtils.camelToUnderline(field), value);
        return this.count(queryWrapper) == 0;
    }

    /**
     * 类型转换成vo
     *
     * @param user 转换前entity
     * @return 转换后vo
     */
    private UserVO convertVo(User user) {
        UserVO vo = new UserVO();
        BeanUtil.copyProperties(user, vo);
        return vo;
    }
}
