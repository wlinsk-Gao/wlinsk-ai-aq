package com.wlinsk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wlinsk.basic.enums.DelStateEnum;
import com.wlinsk.basic.enums.ReviewStatusEnum;
import com.wlinsk.basic.enums.UserRoleEnum;
import com.wlinsk.basic.exception.BasicException;
import com.wlinsk.basic.exception.SysCode;
import com.wlinsk.basic.idGenerator.IdUtils;
import com.wlinsk.basic.transaction.BasicTransactionTemplate;
import com.wlinsk.basic.utils.BasicAuthContextUtils;
import com.wlinsk.mapper.UserMapper;
import com.wlinsk.model.dto.app.req.AddAppReqDTO;
import com.wlinsk.model.dto.app.req.DeleteAppReqDTO;
import com.wlinsk.model.entity.App;
import com.wlinsk.model.entity.User;
import com.wlinsk.service.AppService;
import com.wlinsk.mapper.AppMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppServiceImpl extends ServiceImpl<AppMapper, App>
    implements AppService{

    private final AppMapper appMapper;
    private final UserMapper userMapper;
    private final BasicTransactionTemplate basicTransactionTemplate;

    @Override
    public String addApp(AddAppReqDTO dto) {
        String userId = BasicAuthContextUtils.getUserId();
        User user = userMapper.queryByUserId(userId);
        Optional.ofNullable(user)
                .orElseThrow(() -> new BasicException(SysCode.DATA_NOT_FOUND));

        App app = new App();
        app.init();
        app.setAppId(IdUtils.build(null));
        BeanUtils.copyProperties(dto,app);
        app.setUserId(userId);
        app.setReviewStatus(ReviewStatusEnum.TO_BE_REVIEWED);
        basicTransactionTemplate.execute(action -> {
            if (appMapper.insert(app) != 1){
                throw new BasicException(SysCode.DATABASE_INSERT_ERROR);
            }
            return SysCode.success;
        });
        return app.getAppId();
    }

    @Override
    public void deleteApp(DeleteAppReqDTO reqDTO) {
        String userId = BasicAuthContextUtils.getUserId();
        User user = userMapper.queryByUserId(userId);
        Optional.ofNullable(user).orElseThrow(() -> new BasicException(SysCode.DATA_NOT_FOUND));
        App app = appMapper.queryByAppId(reqDTO.getAppId());
        Optional.ofNullable(app).orElseThrow(() -> new BasicException(SysCode.DATA_NOT_FOUND));
        if (!UserRoleEnum.ADMIN.equals(user.getUserRole()) && !userId.equals(app.getUserId())){
            log.error("无法删除不属于自己的应用");
            throw new BasicException(SysCode.DATA_NOT_FOUND);
        }
        App deleteApp = new App();
        deleteApp.setUpdateTime(new Date());
        deleteApp.setVersion(app.getVersion());
        deleteApp.setDelState(DelStateEnum.DEL);
        deleteApp.setAppId(app.getAppId());
        basicTransactionTemplate.execute(action -> {
            if (appMapper.deleteByAppId(deleteApp) != 1){
                throw new BasicException(SysCode.DATABASE_DELETE_ERROR);
            }
            return SysCode.success;
        });
    }
}




