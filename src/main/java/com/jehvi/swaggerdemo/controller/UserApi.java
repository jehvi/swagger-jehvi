package com.jehvi.swaggerdemo.controller;

import com.jehvi.swaggerdemo.common.CommonResult;
import com.jehvi.swaggerdemo.dto.User;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * @author: lijh
 * @date: 2020/8/19
 * @package: com.jehvi.swaggerdemo.controller
 * @description: 使用API接口可以把swagger注解与controller更好的剥离开
 * restful 增删改查样例
 * 访问地址  http://ip:port/swagger-ui.html#/
 */
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
