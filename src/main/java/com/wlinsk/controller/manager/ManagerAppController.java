package com.wlinsk.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wlinsk.basic.Result;
import com.wlinsk.model.dto.IPageReq;
import com.wlinsk.model.dto.app.req.ManagerReviewAddReqDTO;
import com.wlinsk.model.dto.app.req.ManagerUpdateAppReqDTO;
import com.wlinsk.model.entity.App;
import com.wlinsk.service.manager.ManagerAppService;
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
@RequestMapping("/manager/app")
@RequiredArgsConstructor
@Api(value = "管理端-应用相关")
public class ManagerAppController {

    private final ManagerAppService managerAppService;

    @ApiOperation(value = "更新应用")
    @PostMapping("/update")
    public Result<Void> updateApp(@RequestBody ManagerUpdateAppReqDTO reqDTO){
        managerAppService.updateApp(reqDTO);
        return Result.ok();
    }
    @ApiOperation(value = "更新审核")
    @PostMapping("/review")
    public Result<Void> reviewApp(@RequestBody ManagerReviewAddReqDTO reqDTO){
        managerAppService.reviewApp(reqDTO);
        return Result.ok();
    }
    @ApiOperation(value = "分页查询")
    @PostMapping("/queryPage")
    public Result<IPage<App>> queryPage(@RequestBody IPageReq req){
        IPage<App> result = managerAppService.queryPage(req);
        return Result.ok(result);
    }


}
