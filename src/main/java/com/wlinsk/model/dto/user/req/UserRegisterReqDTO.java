package com.wlinsk.model.dto.user.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/5/23
 */
@Data
public class UserRegisterReqDTO implements Serializable {
    private static final long serialVersionUID = 726417202538994018L;
    @Pattern(regexp = "^\\w{3,20}$",message = "长度在3-20之间，只能包含字母、数字和下划线")
    @NotBlank(message = "用户名不能为空")
    private String userAccount;
    @Pattern(regexp = "^[a-zA-Z]\\w{7,15}$",message = "以字母开头，长度在8-16之间，只能包含字母、数字和下划线")
    @NotBlank(message = "密码不能为空")
    private String userPassword;
    @Pattern(regexp = "^[a-zA-Z]\\w{7,15}$",message = "以字母开头，长度在8-16之间，只能包含字母、数字和下划线")
    @NotBlank(message = "确认密码不能为空")
    private String checkPassword;
}
