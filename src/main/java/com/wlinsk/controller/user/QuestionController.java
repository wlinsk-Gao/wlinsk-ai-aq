package com.wlinsk.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wlinsk.basic.Result;
import com.wlinsk.model.dto.question.req.*;
import com.wlinsk.model.dto.question.resp.AiGenerateQuestionRespDTO;
import com.wlinsk.model.dto.question.resp.QueryQuestionPageRespDTO;
import com.wlinsk.service.user.QuestionService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @Author: wlinsk
 * @Date: 2024/5/28
 */
@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
@Api(value = "应用题目相关")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/add")
    public Result<Void> addQuestion(@Validated @RequestBody AddQuestionReqDTO reqDTO){
        questionService.addQuestion(reqDTO);
        return Result.ok();
    }
    @PostMapping("/update")
    public Result<Void> updateQuestion(@Validated @RequestBody UpdateQuestionReqDTO reqDTO){
        questionService.updateQuestion(reqDTO);
        return Result.ok();
    }
    @PostMapping("/queryPage")
    public Result<IPage<QueryQuestionPageRespDTO>> queryPage(@Validated @RequestBody QueryQuestionPageReqDTO reqDTO){
        IPage<QueryQuestionPageRespDTO> result = questionService.queryPage(reqDTO);
        return Result.ok(result);
    }
    @PostMapping("/aiGenerate")
    public Result<AiGenerateQuestionRespDTO> aiGenerateQuestion(@Validated @RequestBody AiGenerateQuestionReqDTO reqDTO){
        AiGenerateQuestionRespDTO result = questionService.aiGenerateQuestion(reqDTO);
        return Result.ok(result);
    }

    /**
     * sse单向数据流
     * @param reqDTO
     * @return
     */
    @GetMapping("/aiGenerate/sse")
    public SseEmitter aiGenerateSSE(AiGenerateQuestionSSEReqDTO reqDTO){
        return questionService.aiGenerateSSE(reqDTO);
    }

}
