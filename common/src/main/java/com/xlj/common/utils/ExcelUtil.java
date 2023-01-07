package com.xlj.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.xlj.common.exception.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

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
            bean = temp.getClass();
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


        // 设置单个单元格的样式 当然样式 很多的话 也可以用注解等方式。
        WriteCellStyle writeCellStyleData = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.
        writeCellStyleData.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景绿色
        writeCellStyleData.setFillForegroundColor(IndexedColors.GREEN.getIndex());

        // 写法3
        // 这里 需要指定写用哪个class去写
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), bean).build()) {
            WriteSheet writeSheet = EasyExcel
                    .writerSheet("模板")
                    // 自动列宽
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    // 头部样式
                    .registerWriteHandler(new CellWriteHandler() {
                        @Override
                        public void afterCellDispose(CellWriteHandlerContext context) {
                            // 当前事件会在 数据设置到poi的cell里面才会回调
                            // 判断不是头的情况 如果是fill 的情况 这里会==null 所以用not true
                            if (Objects.nonNull(context.getHead()) && context.getHead()) {
                                // 第一个单元格
                                // 只要不是头 一定会有数据 当然fill的情况 可能要context.getCellDataList() ,这个需要看模板，因为一个单元格会有多个 WriteCellData
                                WriteCellData<?> cellData = context.getFirstCellData();
                                // 这里需要去cellData 获取样式
                                // 很重要的一个原因是 WriteCellStyle 和 dataFormatData绑定的 简单的说 比如你加了 DateTimeFormat
                                // ，已经将writeCellStyle里面的dataFormatData 改了 如果你自己new了一个WriteCellStyle，可能注解的样式就失效了
                                // 然后 getOrCreateStyle 用于返回一个样式，如果为空，则创建一个后返回
                                WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                                // 字体
                                WriteFont writeFont = new WriteFont();
                                writeFont.setFontHeightInPoints((short)14);
                                writeFont.setBold(true);
                                writeCellStyle.setWriteFont(writeFont);

                            }
                        }
                    })
                    .build();
            //  写出数据
            excelWriter.write(sourceList, writeSheet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
