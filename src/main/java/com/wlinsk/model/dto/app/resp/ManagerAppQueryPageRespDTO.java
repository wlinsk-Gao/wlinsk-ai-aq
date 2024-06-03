package com.wlinsk.model.dto.app.resp;

import com.wlinsk.basic.enums.AppTypeEnum;
import com.wlinsk.basic.enums.ReviewStatusEnum;
import com.wlinsk.basic.enums.ScoringStrategyEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: wlinsk
 * @Date: 2024/5/27
 */
@Data
public class ManagerAppQueryPageRespDTO implements Serializable {
    private static final long serialVersionUID = 7617938546490260558L;
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
}
