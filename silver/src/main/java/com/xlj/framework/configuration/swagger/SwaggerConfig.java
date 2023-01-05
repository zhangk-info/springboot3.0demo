package com.xlj.framework.configuration.swagger;//package com.xlj.configuration.swagger;
//
//import io.swagger.v3.oas.models.annotations.OpenAPI30;
//import org.springdoc.core.models.GroupedOpenApi;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
///**
// * swagger配置
// * 改到使用yml配置方式
// * @author zhangkun
// */
//@Configuration
//public class SwaggerConfig {
//
//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .displayName("所有api")
//                .group("all")
//                .pathsToMatch("/**")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi adminApi() {
//        return GroupedOpenApi.builder()
//                .displayName("管理")
//                .group("admin")
//                .pathsToMatch("/admin/**")
//                .build();
//    }
//}
