package com.wlinsk.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlinsk.model.entity.ScoringResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.wlinsk.model.entity.ScoringResult
 */
public interface ScoringResultMapper extends BaseMapper<ScoringResult> {

    ScoringResult queryById(@Param("resultId") String resultId);

    int updateScoringResult(@Param("scoringResult") ScoringResult scoringResult);

    IPage<ScoringResult> queryPage(Page<ScoringResult> page, @Param("appId") String appId,
                                   @Param("resultName") String resultName, @Param("resultDesc") String resultDesc);

    List<ScoringResult> queryByAppIdOrderByScoreRange(@Param("appId") String appId);

    int deleteScoringResult(@Param("scoringResult") ScoringResult scoringResult);
}




