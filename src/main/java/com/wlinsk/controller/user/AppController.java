package com.wlinsk.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wlinsk.basic.Result;
import com.wlinsk.model.dto.app.req.AddAppReqDTO;
import com.wlinsk.model.dto.app.req.DeleteAppReqDTO;
import com.wlinsk.model.dto.app.req.QueryAppPageReqDTO;
import com.wlinsk.model.dto.app.req.UpdateAppReqDTO;
import com.wlinsk.model.dto.app.resp.QueryAppDetailsRespDTO;
import com.wlinsk.model.dto.app.resp.QueryAppPageRespDTO;
import com.wlinsk.service.user.AppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: wlinsk
 * @Date: 2024/5/23
 */
@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
@Api(value = "应用相关")
public class AppController {
    private final AppService appService;

    @ApiOperation(value = "添加应用")
    @PostMapping("/add")
    public Result<String> addApp(@Validated @RequestBody AddAppReqDTO dto){
        String appId = appService.addApp(dto);
        return Result.ok(appId);
    }
    @ApiOperation(value = "修改应用")
    @PostMapping("/update")
    public Result<Void> updateApp(@Validated @RequestBody UpdateAppReqDTO reqDTO){
        appService.updateApp(reqDTO);
        return Result.ok();
    }

    @ApiOperation(value = "删除应用")
    @PostMapping("/delete")
    public Result<Void> deleteApp(@Validated @RequestBody DeleteAppReqDTO reqDTO){
        appService.deleteApp(reqDTO);
        return Result.ok();
    }
    @ApiOperation(value = "分页获取应用列表")
    @PostMapping("/queryPage")
    public Result<IPage<QueryAppPageRespDTO>> queryPage(@Validated @RequestBody QueryAppPageReqDTO reqDTO){
        IPage<QueryAppPageRespDTO> result = appService.queryPage(reqDTO);
        return Result.ok(result);
    }

    @ApiOperation(value = "根据id获取应用详情")
    @PostMapping("/queryById/{appId}")
    public Result<QueryAppDetailsRespDTO> queryById(@PathVariable("appId")String appId){
        QueryAppDetailsRespDTO respDTO = appService.queryById(appId);
        return Result.ok(respDTO);
    }
}
