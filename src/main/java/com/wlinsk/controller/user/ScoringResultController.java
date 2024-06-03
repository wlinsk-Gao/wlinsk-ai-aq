package com.wlinsk.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wlinsk.basic.Result;
import com.wlinsk.model.dto.scoringResult.req.AddScoringResultReqDTO;
import com.wlinsk.model.dto.scoringResult.req.QueryScoringResultPageReqDTO;
import com.wlinsk.model.dto.scoringResult.req.UpdateScoringResultReqDTO;
import com.wlinsk.model.dto.scoringResult.resp.QueryScoringResultPageRespDTO;
import com.wlinsk.service.user.ScoringResultService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: wlinsk
 * @Date: 2024/5/29
 */
@RestController
@RequestMapping("/scoringResult")
@RequiredArgsConstructor
@Api(value = "评分结果相关")
public class ScoringResultController {
    private final ScoringResultService scoringResultService;
    @PostMapping("/add")
    public Result<Void> addScoringResult(@Validated @RequestBody AddScoringResultReqDTO reqDTO){
        scoringResultService.addScoringResult(reqDTO);
        return Result.ok();
    }
    @PostMapping("/update")
    public Result<Void> updateScoringResult(@Validated @RequestBody UpdateScoringResultReqDTO reqDTO){
        scoringResultService.updateScoringResult(reqDTO);
        return Result.ok();
    }
    @PostMapping("/queryPage")
    public Result<IPage<QueryScoringResultPageRespDTO>> queryPage(@Validated @RequestBody QueryScoringResultPageReqDTO reqDTO){
        IPage<QueryScoringResultPageRespDTO> result = scoringResultService.queryPage(reqDTO);
        return Result.ok(result);
    }
    @PostMapping("/deleteById/{resultId}")
    public Result<Void> deleteById(@PathVariable("resultId") String resultId){
        scoringResultService.deleteById(resultId);
        return Result.ok();
    }
}
