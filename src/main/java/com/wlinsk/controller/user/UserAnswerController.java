package com.wlinsk.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wlinsk.basic.Result;
import com.wlinsk.model.dto.answer.req.AddUserAnswerReqDTO;
import com.wlinsk.model.dto.answer.req.QueryUserAnswerPageReqDTO;
import com.wlinsk.model.dto.answer.resp.QueryUserAnswerRecordDetailsRespDTO;
import com.wlinsk.service.user.UserAnswerRecordService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: wlinsk
 * @Date: 2024/5/30
 */
@RestController
@RequestMapping("/userAnswer")
@RequiredArgsConstructor
@Api(value = "用户答题相关")
public class UserAnswerController {
    private final UserAnswerRecordService userAnswerRecordService;
    @PostMapping("/add")
    public Result<String> addUserAnswer(@Validated @RequestBody AddUserAnswerReqDTO reqDTO){
        String answerId = userAnswerRecordService.addUserAnswer(reqDTO);
        return Result.ok(answerId);
    }
    @PostMapping("/queryById/{recordId}")
    public Result<QueryUserAnswerRecordDetailsRespDTO> queryById(@PathVariable("recordId") String recordId){
        QueryUserAnswerRecordDetailsRespDTO result = userAnswerRecordService.queryById(recordId);
        return Result.ok(result);
    }
    @PostMapping("/queryPage")
    public Result<IPage<QueryUserAnswerRecordDetailsRespDTO>> queryPage(@Validated @RequestBody QueryUserAnswerPageReqDTO reqDTO){
        IPage<QueryUserAnswerRecordDetailsRespDTO> result = userAnswerRecordService.queryPage(reqDTO);
        return Result.ok(result);
    }
    @PostMapping("/deleteById/{recordId}")
    public Result<Void> deleteById(@PathVariable("recordId") String recordId){
        userAnswerRecordService.deleteById(recordId);
        return Result.ok();
    }
}
