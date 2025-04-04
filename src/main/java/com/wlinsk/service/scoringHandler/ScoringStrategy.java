package com.wlinsk.service.scoringHandler;

import com.wlinsk.model.entity.App;
import com.wlinsk.model.entity.ScoringResult;

import java.util.List;

/**
 * @Author: wlinsk
 * @Date: 2024/5/30
 */
public interface ScoringStrategy {
    ScoringResult doScore(List<String> choices, App app);
}
