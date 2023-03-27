package com.xlj.system.controller;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xlj.common.entity.DataResp;
import com.xlj.common.constants.HttpStatus;
import com.xlj.framework.configuration.auth.common.LoginUser;
import com.xlj.system.page.PageDomain;
import com.xlj.system.page.TableDataInfo;
import com.xlj.system.page.TableSupport;
import com.xlj.framework.configuration.auth.SecurityUtils;
import com.xlj.system.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * web层通用数据处理
 *
 * @author ruoyi
 */
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtil.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageUtils.startPage();
    }

    /**
     * 设置请求排序数据
     */
    protected void startOrderBy() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (!StringUtils.isNotEmpty(pageDomain.getOrderBy())) {
            return;
        }
        String orderBy = pageDomain.getOrderBy();
        PageHelper.orderBy(orderBy);
    }

    /**
     * 清理分页的线程变量
     */
    protected void clearPage() {
        PageUtils.clearPage();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 返回成功
     */
    public DataResp success() {
        return DataResp.success();
    }

    /**
     * 返回失败消息
     */
    public DataResp error() {
        return DataResp.error();
    }

    /**
     * 返回成功消息
     */
    public DataResp success(String message) {
        return DataResp.success(message);
    }

    /**
     * 返回成功消息
     */
    public DataResp success(Object data) {
        return DataResp.success(data);
    }

    /**
     * 返回失败消息
     */
    public DataResp error(String message) {
        return DataResp.error(message);
    }

    /**
     * 返回警告消息
     */
    public DataResp warn(String message) {
        return DataResp.warn(message);
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected DataResp toAjax(int rows) {
        return rows > 0 ? DataResp.success() : DataResp.error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected DataResp toAjax(boolean result) {
        return result ? success() : error();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return String.format("redirect:{}", url);
    }

    /**
     * 获取用户缓存信息
     */
    public LoginUser getLoginUser() {
        return SecurityUtils.getLoginUser();
    }

    /**
     * 获取登录用户id
     */
    public Long getUserId() {
        return getLoginUser().getUserId();
    }

    /**
     * 获取登录部门id
     */
    public Long getDeptId() {
        return getLoginUser().getDeptId();
    }

    /**
     * 获取登录用户名
     */
    public String getUsername() {
        return getLoginUser().getUsername();
    }
}
