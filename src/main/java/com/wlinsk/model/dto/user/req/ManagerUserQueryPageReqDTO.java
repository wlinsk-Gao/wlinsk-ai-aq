package com.wlinsk.model.dto.user.req;

import com.wlinsk.basic.enums.UserRoleEnum;
import com.wlinsk.model.dto.IPageReq;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/5/26
 */
@Data
public class ManagerUserQueryPageReqDTO extends IPageReq implements Serializable {
    private static final long serialVersionUID = 2798000498922031021L;
    private String userId;
    private String userAccount;
    private String userName;
    private UserRoleEnum userRole;
}
