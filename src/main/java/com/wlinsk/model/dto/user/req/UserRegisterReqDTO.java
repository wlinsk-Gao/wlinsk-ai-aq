package com.wlinsk.model.dto.user.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/5/23
 */
@Data
public class UserRegisterReqDTO implements Serializable {
    private static final long serialVersionUID = 726417202538994018L;
    @NotBlank(message = "用户名不能为空")
    private String userAccount;
    @NotBlank(message = "密码不能为空")
    private String userPassword;
    @NotBlank(message = "确认密码不能为空")
    private String checkPassword;
}
