package com.xlj.framework.interceptor;

import cn.hutool.json.JSONUtil;
import com.xlj.common.annotation.RepeatSubmit;
import com.xlj.common.entity.DataResp;
import com.xlj.common.utils.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 防止重复提交拦截器
 *
 * @author ruoyi
 */
public abstract class RepeatSubmitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null) {
                if (this.isRepeatSubmit(request, annotation)) {
                    DataResp<String> ajaxResult = DataResp.error(annotation.message());
                    ServletUtils.renderString(response, JSONUtil.toJsonStr(ajaxResult));
                    return false;
                }
            } else {
                // 默认post提交的方法都要做重复提交限制
                if (request.getMethod().equalsIgnoreCase(HttpMethod.POST.name())) {
                    annotation = new RepeatSubmit() {
                        @Override
                        public Class<? extends Annotation> annotationType() {
                            return RepeatSubmit.class;
                        }

                        @Override
                        public int interval() {
                            return 3;
                        }

                        @Override
                        public String message() {
                            return "不允许重复提交，请稍候再试";
                        }
                    };
                    if (this.isRepeatSubmit(request, annotation)) {
                        DataResp<String> ajaxResult = DataResp.error(annotation.message());
                        ServletUtils.renderString(response, JSONUtil.toJsonStr(ajaxResult));
                        return false;
                    }
                }
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * 验证是否重复提交由子类实现具体的防重复提交的规则
     *
     * @param request
     * @return
     * @throws Exception
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation);
}
