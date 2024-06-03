package com.wlinsk.model.dto.question.req;

import com.wlinsk.model.dto.IPageReq;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/5/28
 */
@Data
public class QueryQuestionPageReqDTO extends IPageReq implements Serializable {
    private static final long serialVersionUID = 514087585116605521L;
    @NotBlank(message = "应用id不可为空")
    private String appId;
}
