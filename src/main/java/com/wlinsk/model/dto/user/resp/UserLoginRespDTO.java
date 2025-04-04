package com.wlinsk.model.dto.user.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/5/23
 */
@Data
public class UserLoginRespDTO implements Serializable {
    private static final long serialVersionUID = -4978618825395368845L;

    private String userId;
    private String token;
}
