package com.xlj.common.utils;

import com.alibaba.excel.EasyExcel;
import com.xlj.common.exception.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author zhangkun
 */
@Slf4j
public class ExcelUtil {

    /**
     * easyexcel 导出excel
     */
    public static void exportExcel(HttpServletResponse response, List sourceList, String fileName) {
        // 推断list中存储的object类型
        Class bean;
        try {
            Object temp = sourceList.get(0);
            // ParameterizedType参数化类型，即泛型
            ParameterizedType p = ((ParameterizedType) temp.getClass()
                    .getGenericSuperclass());
            //getActualTypeArguments获取参数化类型的数组，泛型可能有多个
            bean = (Class) p.getActualTypeArguments()[0];
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("获取List泛型的Class失败");
        }

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        try {
            EasyExcel.write(response.getOutputStream(), bean).sheet("模板").doWrite(sourceList);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("文件输出失败");
        }
    }
}
