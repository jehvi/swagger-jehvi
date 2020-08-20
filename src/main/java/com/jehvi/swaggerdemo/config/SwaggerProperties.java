package com.jehvi.swaggerdemo.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: lijh
 * @date: 2020/8/18
 * @package: com.jehvi.swaggerdemo.config
 * @description: 获取yml配置信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("swagger")
public class SwaggerProperties {

    private List<String> basePackages = new ArrayList<>(Collections.singletonList("com.jehvi"));

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
