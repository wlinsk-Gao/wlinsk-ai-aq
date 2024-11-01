package com.wlinsk.basic.utils;

import com.wlinsk.basic.enums.ReviewStatusEnum;
import com.wlinsk.basic.enums.UserRoleEnum;
import com.wlinsk.basic.exception.BasicException;
import com.wlinsk.basic.exception.SysCode;
import com.wlinsk.mapper.AppMapper;
import com.wlinsk.mapper.UserMapper;
import com.wlinsk.model.entity.App;
import com.wlinsk.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author: wlinsk
 * @Date: 2024/5/29
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class BusinessValidatorUtils {
    private final AppMapper appMapper;
    private final UserMapper userMapper;

    public void validateHandlerPermission(String appId){
        App app = validateAppInfo(appId);
        validateUserInfo(app.getUserId());
    }
    public App validateAppExist(String appId){
        App app = appMapper.queryByAppId(appId);
        Optional.ofNullable(app).orElseThrow(() -> new BasicException(SysCode.DATA_NOT_FOUND));
        return app;
    }
    public App validateAppInfo(String appId){
        App app = appMapper.queryByAppId(appId);
        Optional.ofNullable(app).orElseThrow(() -> new BasicException(SysCode.DATA_NOT_FOUND));
        if (!ReviewStatusEnum.REVIEW_PASS.equals(app.getReviewStatus())){
            throw new BasicException(SysCode.APP_NOT_REVIEW);
        }
        return app;
    }
    public User validateUserInfo(String targetUserId){
        String currentUserId = BasicAuthContextUtils.getUserId();
        User user = userMapper.queryByUserId(currentUserId);
        if (!UserRoleEnum.ADMIN.equals(user.getUserRole()) && !targetUserId.equals(currentUserId)){
            throw new BasicException(SysCode.SYSTEM_NO_PERMISSION);
        }
        return user;
    }

}
