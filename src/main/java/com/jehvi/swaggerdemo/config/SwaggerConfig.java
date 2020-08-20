package com.jehvi.swaggerdemo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

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
