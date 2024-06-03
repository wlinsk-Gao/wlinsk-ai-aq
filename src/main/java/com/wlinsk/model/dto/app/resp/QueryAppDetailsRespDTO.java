package com.wlinsk.model.dto.app.resp;

import com.wlinsk.basic.enums.AppTypeEnum;
import com.wlinsk.basic.enums.ReviewStatusEnum;
import com.wlinsk.basic.enums.ScoringStrategyEnum;
import com.wlinsk.model.dto.user.resp.QueryUserDetailRespDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: wlinsk
 * @Date: 2024/5/27
 */
@Data
public class QueryAppDetailsRespDTO implements Serializable {
    private static final long serialVersionUID = -2040340319740040024L;
    private String appId;

    private String appName;

    private String appDesc;

    private String appIcon;

    private AppTypeEnum appType;

    private ScoringStrategyEnum scoringStrategy;

    private ReviewStatusEnum reviewStatus;

    private String reviewMessage;

    private String reviewerId;

    private Date reviewTime;

    private String userId;

    private Date createTime;

    private Date updateTime;

    private QueryUserDetailRespDTO userInfo;
}
