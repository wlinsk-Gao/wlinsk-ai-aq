package com.wlinsk.controller;

import com.wlinsk.basic.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wlinsk
 * @Date: 2024/5/22
 */
@Slf4j
@RestController()
@RequestMapping("/test")
@Api(value = "测试接口",tags = {"测试swagger"})
public class TestController {
    @ApiOperation("testGet")
    @GetMapping("/testGet")
    public Result<String> testGet() {
        log.info("testGet");
        return Result.ok("testGet");
    }
}
