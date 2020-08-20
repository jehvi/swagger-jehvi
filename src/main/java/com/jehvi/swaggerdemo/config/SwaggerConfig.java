package com.jehvi.swaggerdemo.config;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * @author: lijh
 * @date: 2020/8/18
 * @package: com.jehvi.swaggerdemo.config
 * @description: swagger 配置类
 */
@Configuration
public class SwaggerConfig {

    @Bean
    @ConditionalOnMissingBean
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }


    @Bean
    public Docket customImplementation(SwaggerProperties swaggerProperties){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //扩展支持list
                //.apis(RequestHandlerSelectors.basePackage("com.jehvi.swaggerdemo.controller"))
                .apis(basePackages(swaggerProperties.getBasePackages()))
                .build()
                .directModelSubstitute(org.threeten.bp.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.threeten.bp.OffsetDateTime.class, java.util.Date.class)
                .apiInfo(getApiInfo(swaggerProperties));
    }

    /**
     * 通过yml配置获取基础信息
     * @return
     */
    private ApiInfo getApiInfo(SwaggerProperties swaggerProperties){
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .version(swaggerProperties.getVersion())
                .contact(new Contact(swaggerProperties.getContact().getName()
                        ,swaggerProperties.getContact().getUrl()
                        ,swaggerProperties.getContact().getEmail()))
                .build();
    }


    /**
     * 获取包集合
     * 参考 RequestHandlerSelectors 底层方法重写
     * @param basePackages 多个包名集合
     */
    public static Predicate<RequestHandler> basePackages(final List<String> basePackages) {
        return input -> declaringClass(input).transform(handlerPackage(basePackages)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final List<String> basePackages) {
        return input -> {
            // 循环匹配
            for (String strPackage : basePackages) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }
}
