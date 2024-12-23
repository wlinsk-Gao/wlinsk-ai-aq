package com.wlinsk.model.dto.user.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/12/23
 */
@Data
public class UpdateUserProfileReqDTO implements Serializable {
    private static final long serialVersionUID = -1022681149263792422L;
    private String userProfile;
}
