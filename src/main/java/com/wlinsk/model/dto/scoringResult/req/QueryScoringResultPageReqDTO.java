package com.wlinsk.model.dto.scoringResult.req;

import com.wlinsk.model.dto.IPageReq;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/5/29
 */
@Data
public class QueryScoringResultPageReqDTO extends IPageReq implements Serializable {
    private static final long serialVersionUID = 3481071716813538906L;
    @NotBlank(message = "应用不能为空")
    private String appId;
    private String resultName;
    private String resultDesc;

}
