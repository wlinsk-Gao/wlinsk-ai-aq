package com.wlinsk.model.dto.user.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/5/23
 */
@Data
public class UserLoginReqDTO implements Serializable {
    private static final long serialVersionUID = -1650140655438129536L;
    @NotBlank(message = "用户名不能为空")
    private String userAccount;
    @NotBlank(message = "密码不能为空")
    private String userPassword;
}
