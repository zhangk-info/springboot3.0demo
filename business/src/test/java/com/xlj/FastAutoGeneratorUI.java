package com.xlj;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.SQLException;
import java.util.Collections;

/**
 * <p>
 * 快速生成
 * </p>
 *
 * @author lanjerry
 * @since 2021-09-16
 */
public class FastAutoGeneratorUI {

    /**
     * 执行 run
     */
    public static void main(String[] args) throws SQLException {
        FastAutoGenerator.create("jdbc:mysql://43.153.19.82:3306/demo?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai",
                        "root", "zhangkun..123")
                .globalConfig(builder -> {
                    builder.author("zhangkun") // 设置作者
                            .enableSpringdoc() // 开启 swagger 模式
                            .outputDir("C:\\workspace\\springboot3.0demo\\business\\src\\test\\java"); // 指定输出目录
                })
                .templateConfig(builder -> {
                    builder.disable(TemplateType.ENTITY)
                            .disable(TemplateType.SERVICE)
                            .disable(TemplateType.SERVICE_IMPL)
                            .disable(TemplateType.MAPPER)
                            .disable(TemplateType.XML)
                            .disable(TemplateType.CONTROLLER);
                })
                .packageConfig(builder -> {
                    builder
                            .parent("ui") // 设置父包名
                            .moduleName("") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "C:\\workspace\\springboot3.0demo\\business\\src\\test\\java\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    // 配置文件可覆盖
                    builder.entityBuilder()
                            .enableLombok()
                            .logicDeleteColumnName("is_delete")
                            .superClass(com.xlj.common.entity.BaseEntity.class)
                            .addSuperEntityColumns("create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by")
                            .enableFileOverride();
                    builder.serviceBuilder().enableFileOverride();
                    builder.mapperBuilder().enableFileOverride();
                    builder.controllerBuilder().enableRestStyle().enableHyphenStyle().enableFileOverride();
                    builder.addInclude("feedback") // 设置需要生成的表名
                            .addTableSuffix("s")
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .injectionConfig(consumer -> {
                    // uiIndex
                    consumer.customFile(builder -> {
                        builder
                                .fileName("_index.vue")
                                .packageName("")
                                .enableFileOverride()
                                .templatePath("/templates-customizer/uiIndex.java.ftl");
                    });
                    // uiRoute
                    consumer.customFile(builder -> {
                        builder
                                .fileName(".js")
                                .packageName("")
                                .enableFileOverride()
                                .templatePath("/templates-customizer/uiRoute.java.ftl");
                    });
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}