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
public class FastAutoGeneratorTest {

    /**
     * 执行 run
     */
    public static void main(String[] args) throws SQLException {
        FastAutoGenerator.create("jdbc:mysql://43.153.19.82:3306/silver?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai",
                        "root", "zhangkun..123")
                .globalConfig(builder -> {
                    builder.author("zhangkun") // 设置作者
                            .enableSpringdoc() // 开启 swagger 模式
                            .outputDir("C:\\workspace\\demo\\silver\\src\\test\\java"); // 指定输出目录
                })
                .templateConfig(builder -> {
                    builder.disable(TemplateType.ENTITY)
                            .entity("/templates-customizer/entity.java")
                            .service("/templates-customizer/service.java")
                            .serviceImpl("/templates-customizer/serviceImpl.java")
                            .mapper("/templates-customizer/mapper.java")
                            .xml("/templates-customizer/mapper.xml")
                            .controller("/templates-customizer/controller.java");
                })
                .packageConfig(builder -> {
                    builder
                            .parent("generator") // 设置父包名
                            .moduleName("") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "C:\\workspace\\demo\\silver\\src\\test\\java\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    // 配置文件可覆盖
                    builder.entityBuilder()
                            .enableLombok()
                            .logicDeleteColumnName("is_delete")
                            .superClass(com.xlj.common.entity.BaseEntity.class)
                            .addSuperEntityColumns("created_at", "updated_at", "deleted_at")
                            .enableFileOverride();
                    builder.serviceBuilder().enableFileOverride();
                    builder.mapperBuilder().enableFileOverride();
                    builder.controllerBuilder().enableRestStyle().enableHyphenStyle().enableFileOverride();
                    builder.addInclude("users") // 设置需要生成的表名
                            .addTableSuffix("s")
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .injectionConfig(consumer -> {
                    // VO
                    consumer.customFile(builder -> {
                        builder
                                .fileName("VO.java")
                                .packageName("response")
                                .enableFileOverride()
                                .templatePath("/templates-customizer/entityVO.java.ftl");
                    });
                    // DTO
                    consumer.customFile(builder -> {
                        builder
                                .fileName("DTO.java")
                                .packageName("request")
                                .enableFileOverride()
                                .templatePath("/templates-customizer/entityDTO.java.ftl");
                    });
                    // QueryDTO
                    consumer.customFile(builder -> {
                        builder
                                .fileName("QueryDTO.java")
                                .packageName("request")
                                .enableFileOverride()
                                .templatePath("/templates-customizer/entityQueryDTO.java.ftl");
                    });
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}