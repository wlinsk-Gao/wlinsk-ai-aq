package com.wlinsk.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wlinsk.basic.Result;
import com.wlinsk.model.dto.user.req.ManagerUserQueryPageReqDTO;
import com.wlinsk.model.dto.user.resp.ManagerUserQueryPageRespDTO;
import com.wlinsk.service.manager.ManagerUserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: wlinsk
 * @Date: 2024/5/26
 */
@RestController
@RequestMapping("/manager/user")
@RequiredArgsConstructor
@Api(value = "管理端-用户相关接口")
public class ManagerUserController {

    private final ManagerUserService managerUserService;
    @PostMapping("/queryPage")
    public Result<IPage<ManagerUserQueryPageRespDTO>> queryPage(@Validated @RequestBody ManagerUserQueryPageReqDTO reqDTO){
        IPage<ManagerUserQueryPageRespDTO> result = managerUserService.queryPage(reqDTO);
        return Result.ok(result);
    }
    @PostMapping("/deleteById/{userId}")
    public Result<Void> deleteById(@PathVariable("userId") String userId){
        managerUserService.deleteById(userId);
        return Result.ok();
    }
}
