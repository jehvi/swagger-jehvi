package com.jehvi.swaggerdemo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author: lijh
 * @date: 2020/8/18
 * @package: com.jehvi.swaggerdemo.dto
 * @description: 对象 可以不做非空控制 可以在controller @ApiParam 中实现。避免到处做required 影响测试 此处类演示暂时加上参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户信息对象")
public class User {
    @ApiModelProperty(value = "用户ID",required = false)
    Long id;
    @ApiModelProperty(value = "用户名称",required = false)
    String userName;
    @ApiModelProperty(value = "用户英文名称", required = false)
    String userEName;
}
