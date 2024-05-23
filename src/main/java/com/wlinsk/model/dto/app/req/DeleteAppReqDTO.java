package com.wlinsk.model.dto.app.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/5/23
 */
@Data
public class DeleteAppReqDTO implements Serializable {
    private static final long serialVersionUID = 2735763225201070039L;
    @NotBlank(message = "应用id不可为空")
    private String appId;

}
