package com.wlinsk.model.dto.question.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/6/2
 */
@Data
public class AiGenerateQuestionSSEReqDTO extends AiGenerateQuestionReqDTO implements Serializable {
    private static final long serialVersionUID = -7360599485455436134L;
    private String token;
}
