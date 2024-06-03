package com.wlinsk.model.dto.answer.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: wlinsk
 * @Date: 2024/5/30
 */
@Data
public class AddUserAnswerReqDTO implements Serializable {
    private static final long serialVersionUID = -2036746596342647570L;
    @NotBlank(message = "应用id不可为空")
    private String appId;
    @NotEmpty(message = "答案不可为空")
    private List<String> choices;
}
