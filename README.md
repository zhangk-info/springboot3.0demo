## 测试测试

### 主要组件：

* spring boot 3.0.0
* spring authorization server1.0
* flyway
* mysql
* mybatis-plus
```
    mybatis-plus 更新为空方式
    1. 调整全局策略
    2. 调整注解属性
    3. updateWrapper set(xxx,null)
```

#### 文档地址： http://localhost:92/doc.html#/home


##### swagger3
```
swagger2	        OpenAPI 3	                        注解位置
@Api	            @Tag(name = “接口类描述”)	            Controller 类上
@ApiOperation	    @Operation(summary =“接口方法描述”)	Controller 方法上
@ApiImplicitParams	@Parameters	                        Controller 方法上
@ApiImplicitParam	@Parameter(description=“参数描述”)	Controller 方法上 @Parameters 里
@ApiParam	        @Parameter(description=“参数描述”)	Controller 方法的参数上
@ApiIgnore	        @Parameter(hidden = true) 或 @Operation(hidden = true) 或 @Hidden	-
@ApiModel	        @Schema	                            DTO类上
@ApiModelProperty	@Schema	                            DTO属性上
```