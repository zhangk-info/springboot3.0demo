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



#### jib
* 跳过测试并http方式跳过证书认证
  mvn clean compile jib:build -D maven.test.skip=true -D sendCredentialsOverHttp=true

#### 构建过程
1. mvn clean
2. mvn clean install -D maven.test.skip=true
3. mvn compile jib:build -D maven.test.skip=true -D sendCredentialsOverHttp=true -pl business

#### 打包
1. mvn clean
2. mvn clean install -D maven.test.skip=true
3. mvn package -DskipTests -pl demo



#### 测试环境数据库
docker run -p 33306:3306 --name mariadb-db -v /data/mysql/db/conf/:/etc/mysql/conf.d/ -v /data/mysql/db/logs:/var/log/mysql -v /data/mysql/db/data:/var/lib/mysql --restart=always --privileged=true -e MYSQL_ROOT_PASSWORD=RlcGFy36 -d mariadb:latest