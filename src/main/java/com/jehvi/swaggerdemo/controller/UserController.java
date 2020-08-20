package com.jehvi.swaggerdemo.controller;

import com.jehvi.swaggerdemo.common.CommonResult;
import com.jehvi.swaggerdemo.dto.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lijh
 * @date: 2020/8/18
 * @package: com.jehvi.swaggerdemo.controller
 * @description:
 */
@Api(value = "入门级",tags = {"bate-用户管理API"})
@RestController
public class UserController {

    public static List<User> userList = new ArrayList<>();

    static {
        userList.add(new User(1L,"李金徽","jehvi"));
        userList.add(new User(2L,"swagger","SpringBoot"));
    }

    @ApiOperation(value = "All user",notes = "获取所有用户")
    @GetMapping("/u")
    public CommonResult<User> getUsers(@Valid @RequestBody  User user){
        Map<String, Object> umap = new HashMap<>();
        umap.put("users",userList);
        return CommonResult.data(user);
    }
}
