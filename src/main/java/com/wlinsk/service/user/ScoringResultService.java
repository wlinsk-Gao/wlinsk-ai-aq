package com.wlinsk.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wlinsk.model.dto.scoringResult.req.AddScoringResultReqDTO;
import com.wlinsk.model.dto.scoringResult.req.QueryScoringResultPageReqDTO;
import com.wlinsk.model.dto.scoringResult.req.UpdateScoringResultReqDTO;
import com.wlinsk.model.dto.scoringResult.resp.QueryScoringResultPageRespDTO;
import com.wlinsk.model.entity.ScoringResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface ScoringResultService extends IService<ScoringResult> {

    void addScoringResult(AddScoringResultReqDTO reqDTO);

    void updateScoringResult(UpdateScoringResultReqDTO reqDTO);

    IPage<QueryScoringResultPageRespDTO> queryPage(QueryScoringResultPageReqDTO reqDTO);

    void deleteById(String resultId);

}
