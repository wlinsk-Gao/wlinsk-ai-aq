package com.wlinsk.model.dto.app.req;

import com.wlinsk.basic.enums.AppTypeEnum;
import com.wlinsk.basic.enums.ScoringStrategyEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/5/23
 */
@Data
public class AddAppReqDTO implements Serializable {
    private static final long serialVersionUID = 2482837485740158000L;
    /**
     * 应用名
     */
    @NotBlank(message = "应用名称不能为空")
    private String appName;

    /**
     * 应用描述
     */
    @NotBlank(message = "应用描述不能为空")
    private String appDesc;

    /**
     * 应用图标
     */
    private String appIcon;

    /**
     * 应用类型（0-得分类，1-测评类）
     */
    @NotNull(message = "应用类别不能为空")
    private AppTypeEnum appType;

    /**
     * 评分策略（0-自定义，1-AI）
     */
    @NotNull(message = "应用评分策略不能为空")
    private ScoringStrategyEnum scoringStrategy;
}
