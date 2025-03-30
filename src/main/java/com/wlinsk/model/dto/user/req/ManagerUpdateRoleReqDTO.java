package com.wlinsk.model.dto.user.req;

import com.wlinsk.basic.enums.UserRoleEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2025/3/30
 */
@Data
public class ManagerUpdateRoleReqDTO implements Serializable {
    private static final long serialVersionUID = -7657989504116070515L;

    @NotBlank(message = "用户id不可为空")
    private String userId;
    @NotNull(message = "请选择一个角色")
    private UserRoleEnum userRole;
}
