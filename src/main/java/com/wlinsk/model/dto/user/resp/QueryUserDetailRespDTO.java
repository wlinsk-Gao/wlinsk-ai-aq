package com.wlinsk.model.dto.user.resp;

import com.wlinsk.basic.enums.UserRoleEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/5/23
 */
@Data
public class QueryUserDetailRespDTO implements Serializable {
    private static final long serialVersionUID = 2116410352516364154L;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private UserRoleEnum userRole;
}
