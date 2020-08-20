# swagger-jevhi
## 项目集成 Swagger 管理 API 文档 开发手记
Swagger 是一个规范且完整的框架，用于生成、描述、调用和可视化 RESTful 风格的 Web 服务。

Swagger 的目标是对 REST API 定义一个标准且和语言无关的接口，可以让人和计算机拥有无须访问源码、文档或网络流量监测就可以发现和理解服务的能力。当通过 Swagger 进行正确定义，用户可以理解远程服务并使用最少实现逻辑与远程服务进行交互。与为底层编程所实现的接口类似，Swagger 消除了调用服务时可能会有的猜测。
Swagger 的优势
  支持 API 自动生成同步的在线文档：使用 Swagger 后可以直接通过代码生成文档，不再需要自己手动编写接口文档了，对程序员来说非常方便，可以节约写文档的时间去学习新技术。
  提供 Web 页面在线测试 API：光有文档还不够，Swagger 生成的文档还支持在线测试。参数和格式都定好了，直接在界面上输入参数对应的值即可在线测试接口。

## 项目中集成 Swagger
### 1）创建springboot项目 pom 引入依赖
```
<!--SpringFox dependencies -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>${springfox-version}</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>${springfox-version}</version>
</dependency>

<dependency>
    <groupId>com.github.joschi.jackson</groupId>
    <artifactId>jackson-datatype-threetenbp</artifactId>
    <version>2.6.4</version>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>${lombok.version}</version>
</dependency>
```
### 2）创建主启动类 @EnableSwagger2
```
@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {"com.jehvi"})
public class SwaggerdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwaggerdemoApplication.class, args);
    }

}
```
### 3）创建swagger configuration相关配置类
```
@Configuration
public class SwaggerConfig {

    @Bean
    @ConditionalOnMissingBean
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }


    @Bean
    public Docket customImplementation(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jehvi.swaggerdemo.controller"))
                .build()
                .directModelSubstitute(org.threeten.bp.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.threeten.bp.OffsetDateTime.class, java.util.Date.class)
                .apiInfo(getApiInfo());
    }

    /**
     * 通过yml配置获取基础信息
     * @return
     */
    private ApiInfo getApiInfo(){
        return new ApiInfoBuilder()
                .title(swaggerProperties().getTitle())
                .description(swaggerProperties().getDescription())
                .license(swaggerProperties().getLicense())
                .licenseUrl(swaggerProperties().getLicenseUrl())
                .termsOfServiceUrl(swaggerProperties().getTermsOfServiceUrl())
                .version(swaggerProperties().getVersion())
                .contact(new Contact(swaggerProperties().getContact().getName()
                        ,swaggerProperties().getContact().getUrl()
                        ,swaggerProperties().getContact().getEmail()))
                .build();
    }
}
```
### 获取YML配置类
```
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("swagger")
public class SwaggerProperties {

    /**
     * 标题
     **/
    private String title;
    /**
     * 描述
     **/
    private String description;
    /**
     * 版本
     **/
    private String version;
    /**
     * 许可证
     **/
    private String license;
    /**
     * 许可证URL
     **/
    private String licenseUrl;
    /**
     * 服务条款URL
     **/
    private String termsOfServiceUrl;

    /**
     * 联系人信息
     */
    private Contact contact = new Contact();

    @Data
    @NoArgsConstructor
    public static class Contact {

        /**
         * 联系人
         **/
        private String name = "jehvi 李金徽";
        /**
         * 联系人url
         **/
        private String url = "https://jehvi.com.cn";
        /**
         * 联系人email
         **/
        private String email = "lijinhui001@sina.com";

    }

}
```
### 4）创建yml
```
server:
    port: 8008

#swagger公共信息
swagger:
    title: jehvi 李金徽API接口
    description: API 接口
    license: jehvi
    license-url: http://jehvi.com.cn
    terms-of-service-url: http://jehvi.com.cn
    contact:
        name: jehvi 李金徽
        email: lijinhui001@sina.com
        url: https://jehvi.com.cn
    version: 1.0.0
```
### 5)创建Api接口与controller  此处定义APi接口Controller去实现，在api接口上注入Swagger annontation 与控制器剥离开，便于维护
```
@Api(value = "用户管理API",tags = {"user",})
@RestController
public interface UserApi {

    @ApiOperation(value = "根据用户名称获取用户信息",nickname = "getUserByUserName",notes = "根据用户名称获取用户信息",response = User.class, tags={ "user", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功", response = User.class),
            @ApiResponse(code = 400, message = "参数异常"),
            @ApiResponse(code = 404, message = "用户信息不存在") })
    @GetMapping("/user/{userName}")
    CommonResult<User> getUserByUserName(@PathVariable("userName") String userName);

    @ApiOperation(value = "根据ID获取用户",notes = "根据ID获取用户", tags={ "user", })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", dataType = "Long", paramType = "query", required = true, defaultValue = "1") })
    @ApiResponses({ @ApiResponse(code = 200, message = "OK", response = User.class) })
    @GetMapping("/user")
    CommonResult<User> getUserByUserId(@PathParam("id") Long id);

    /**
     *  nickname 可以不用 value与notes 根据项目需要自定义英文与中文名称
     * @param body
     * @return
     */
    @ApiOperation(value = "创建用户", nickname = "createUser", notes = "创建用户", tags={ "user", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功") })
    @PostMapping("/user")
    CommonResult<User> createUser(@ApiParam(value = "创建用户对象" ,required=true )  @RequestBody User body);


    /**
     * 更新数据 produces 对象传入形式
     * tags 产生接口页面时会生成一个独立的user的说明
     * @param userName
     * @param body
     * @return
     */
    @ApiOperation(value = "更新用户信息", nickname = "updateUser", notes = "更新用户信息", tags={ "user", })
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "参数异常"),
            @ApiResponse(code = 404, message = "用户信息不存在") })
    @PutMapping(value = "/user/{userName}",produces = { "application/xml", "application/json" })
    CommonResult<User> updateUser(@ApiParam(value = "需要更新的用户名称",required=true) @PathVariable("userName") String userName,@ApiParam(value = "更新后的数据" ,required=true )  @RequestBody User body);


    /**
     * 根据名称删除数据
     * @param userName
     * @return
     */
    @ApiOperation(value = "根据用户名称删除用户", nickname = "deleteUser", notes = "根据用户名称删除用户", tags={ "user", })
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "参数异常"),
            @ApiResponse(code = 404, message = "用户信息不存在") })
    @DeleteMapping(value = "/user/{userName}")
    CommonResult<User> deleteUser(@ApiParam(value = "根据用户名称删除对象",required=true) @PathVariable("userName") String userName);
}
```
Controller
```
@Controller
@Slf4j
public class UserExtendController implements UserApi{

    public static HashMap<String, User> userNameMap = new HashMap<>();
    public static HashMap<Long, User> userIdMap = new HashMap<>();

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    public UserExtendController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }
    /**
     * 模拟数据
     */
    static {
        userNameMap.put("李金徽",new User(1L,"李金徽","jehvi"));
        userNameMap.put("刘宇",new User(2L,"刘宇","小胖"));
        userIdMap.put(1L,new User(1L,"李金徽","jehvi"));
        userIdMap.put(2L,new User(2L,"刘宇","小胖"));
    }

    @Override
    public CommonResult<User> getUserByUserName(String userName) {
        User user = userNameMap.get(userName);
        return CommonResult.data(user);
    }

    @Override
    public CommonResult<User> getUserByUserId(Long id) {
        User user = userIdMap.get(id);
        return CommonResult.data(user);
    }

    @Override
    public CommonResult<User> createUser(User body) {
        return CommonResult.success("保存成功");
    }

    @Override
    public CommonResult<User> updateUser(String username, User body) {
        String accept = request.getHeader("Accept");
        if (ObjectUtils.nullSafeEquals(accept,"application/json")) {
            log.info("accept======"+accept);
        }
        return CommonResult.status(true);
    }

    @Override
    public CommonResult<User> deleteUser(String username) {
        return CommonResult.fail(ResultCode.SUCCESS);
    }
}
```
### 公共返回类具体参考common目录，这里就不赘述了
### 最后介绍下Swagger annotation 明细说明
整理swagger annotion 注解
1. Api
@Api 用在类上，说明该类的作用。可以标记一个 Controller 类作为 Swagger 文档资源
```
@Api(value = "用户管理API",tags = {"user",})
@RestController
public interface UserApi
```
tags：接口说明，可以在页面中显示。可以配置多个，当配置多个的时候，在页面中会显示多个接口的信息。此处这样配置就不会在页面展示controller名称。直接展示user。这样可以更好的针对对象展示接口

2. ApiModel
@ApiModel 用在类上，表示对类进行说明，用于实体类中的参数接收说明。
3. ApiModelProperty
@ApiModelProperty() 用于字段，表示对 model 属性的说明。使用方式代码如下所示。
```
@Getter
@Setter
@ToString
@ApiModel(description = "通用应答返回信息")
@NoArgsConstructor
public class CommonResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_SUCCESS_MESSAGE = "操作成功";
    private static final String DEFAULT_FAIL_MESSAGE = "操作失败";
    private static final String DEFAULT_NULL_MESSAGE = "暂无承载数据";

    @ApiModelProperty(value = "状态码", required = true)
    private int code;
    @ApiModelProperty(value = "是否成功", required = true)
    private boolean success;
    @ApiModelProperty(value = "承载数据")
    private T data;
    @ApiModelProperty(value = "返回消息", required = true)
    private String msg;
```
4. ApiOperation
@ApiOperation 用在 Controller 里的方法上，说明方法的作用，每一个接口的定义。
5. ApiParam
@ApiParam 用于 Controller 中方法的参数说明。
6. ApiResponse 和 ApiResponses
@ApiResponse 用于方法上，说明接口响应的一些信息；@ApiResponses 组装了多个 @ApiResponse。使用方式代码如下所示。
```
@ApiOperation(value = "更新用户信息", nickname = "updateUser", notes = "更新用户信息", tags={ "user", })
@ApiResponses(value = {
        @ApiResponse(code = 400, message = "参数异常"),
        @ApiResponse(code = 404, message = "用户信息不存在") })
@PutMapping(value = "/user/{userName}",produces = { "application/xml", "application/json" })
CommonResult<User> updateUser(@ApiParam(value = "需要更新的用户名称",required=true) @PathVariable("userName") String userName,@ApiParam(value = "更新后的数据" ,required=true )  @RequestBody User body);
```
7. ApiImplicitParam 和 ApiImplicitParams
用于方法上，为单独的请求参数进行说明。使用方式代码如下所示。
name：参数名，对应方法中单独的参数名称。
value：参数中文说明。
required：是否必填。
paramType：参数类型，取值为 path、query、body、header、form。
dataType：参数数据类型。
defaultValue：默认值。
```
@ApiOperation(value = "根据ID获取用户",notes = "根据ID获取用户", tags={ "user", })
@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "用户ID", dataType = "Long", paramType = "query", required = true, defaultValue = "1") })
@ApiResponses({ @ApiResponse(code = 200, message = "OK", response = User.class) })
@GetMapping("/user")
CommonResult<User> getUserByUserId(@PathParam("id") Long id);
```
