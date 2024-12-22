package com.wlinsk.controller.user;

import com.wlinsk.basic.Result;
import com.wlinsk.basic.utils.BasicAuthContextUtils;
import com.wlinsk.model.dto.user.req.ThreePartLoginReqDTO;
import com.wlinsk.model.dto.user.req.UpdatePasswordReqDTO;
import com.wlinsk.model.dto.user.req.UserLoginReqDTO;
import com.wlinsk.model.dto.user.req.UserRegisterReqDTO;
import com.wlinsk.model.dto.user.resp.QueryUserDetailRespDTO;
import com.wlinsk.model.dto.user.resp.UserLoginRespDTO;
import com.wlinsk.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: wlinsk
 * @Date: 2024/5/23
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Api(value = "用户相关接口")
public class UserController {
    private final UserService userService;

    @ApiOperation("注册")
    @PostMapping("/register")
    public Result<Void> register(@Validated @RequestBody UserRegisterReqDTO dto){
        userService.register(dto);
        return Result.ok();
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public Result<UserLoginRespDTO> login(@Validated @RequestBody UserLoginReqDTO dto){
        UserLoginRespDTO result = userService.login(dto);
        return Result.ok(result);
    }
    @ApiOperation("三方登录")
    @PostMapping("/threePartLogin")
    public Result<String> threePartLogin(@Validated @RequestBody ThreePartLoginReqDTO dto){
        return Result.ok(userService.threePartLogin(dto));
    }
    @ApiOperation("三方登录回调")
    @PostMapping("/threePartLoginCallback/{code}")
    public Result<UserLoginRespDTO> threePartLoginCallback(@PathVariable("code") String code){
        UserLoginRespDTO userLoginRespDTO = userService.threePartLoginCallback(code);
        return Result.ok(userLoginRespDTO);
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result<Void> logout(){
        userService.logout();
        return Result.ok();
    }

    @ApiOperation("查询用户详情")
    @GetMapping("/queryDetails")
    public Result<QueryUserDetailRespDTO> queryDetails(){
        String userId = BasicAuthContextUtils.getUserId();
        QueryUserDetailRespDTO result = userService.queryById(userId);
        return Result.ok(result);
    }

    @ApiOperation("修改用户信息")
    @PostMapping("/updatePassword")
    public Result<Void> updatePassword(@Validated @RequestBody UpdatePasswordReqDTO dto){
        userService.updatePassword(dto);
        return Result.ok();
    }
}
