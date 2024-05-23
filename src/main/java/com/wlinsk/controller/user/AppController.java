package com.wlinsk.controller.user;

import com.wlinsk.basic.Result;
import com.wlinsk.model.dto.app.req.AddAppReqDTO;
import com.wlinsk.model.dto.app.req.DeleteAppReqDTO;
import com.wlinsk.service.AppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wlinsk
 * @Date: 2024/5/23
 */
@Slf4j
@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
@Api(value = "应用相关")
public class AppController {
    private final AppService appService;

    @ApiOperation(value = "添加应用")
    @PostMapping("/add")
    public Result<String> addApp(@RequestBody AddAppReqDTO dto){
        String appId = appService.addApp(dto);
        return Result.ok(appId);
    }

    @ApiOperation(value = "删除应用")
    @PostMapping("/delete")
    public Result<Void> deleteApp(@RequestBody DeleteAppReqDTO reqDTO){
        appService.deleteApp(reqDTO);
        return Result.ok();
    }
}
