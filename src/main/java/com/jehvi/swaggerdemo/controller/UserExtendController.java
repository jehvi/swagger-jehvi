package com.jehvi.swaggerdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jehvi.swaggerdemo.common.CommonResult;
import com.jehvi.swaggerdemo.common.ResultCode;
import com.jehvi.swaggerdemo.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author: lijh
 * @date: 2020/8/19
 * @package: com.jehvi.swaggerdemo.controller
 * @description: 针对userController 优化
 * 访问地址 http://localhost:8008/swagger-ui.html
 */
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
