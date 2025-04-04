package com.wlinsk.model.dto.answer.resp;

import com.wlinsk.basic.enums.AppTypeEnum;
import com.wlinsk.basic.enums.ScoringStrategyEnum;
import com.wlinsk.model.dto.user.resp.QueryUserDetailRespDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: wlinsk
 * @Date: 2024/5/30
 */
@Data
public class QueryUserAnswerRecordDetailsRespDTO implements Serializable {
    private static final long serialVersionUID = -3632406226448006276L;
    private String recordId;
    private String appId;
    private AppTypeEnum appType;
    private ScoringStrategyEnum scoringStrategy;
    private List<String> choices;
    private String resultId;
    private String resultName;
    private String resultDesc;
    private String resultPicture;
    private Integer resultScore;
    private String userId;
    private Date createTime;
    private QueryUserDetailRespDTO userInfo;
}
