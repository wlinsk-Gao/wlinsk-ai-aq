package com.wlinsk.service.manager.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlinsk.basic.enums.AppTypeEnum;
import com.wlinsk.basic.enums.ScoringStrategyEnum;
import com.wlinsk.basic.exception.BasicException;
import com.wlinsk.basic.exception.SysCode;
import com.wlinsk.basic.transaction.BasicTransactionTemplate;
import com.wlinsk.basic.utils.BasicAuthContextUtils;
import com.wlinsk.basic.utils.BusinessValidatorUtils;
import com.wlinsk.mapper.AppMapper;
import com.wlinsk.mapper.UserMapper;
import com.wlinsk.model.dto.app.req.ManagerAppQueryPageReqDTO;
import com.wlinsk.model.dto.app.req.ManagerReviewAddReqDTO;
import com.wlinsk.model.dto.app.req.ManagerUpdateAppReqDTO;
import com.wlinsk.model.dto.app.resp.ManagerAppQueryPageRespDTO;
import com.wlinsk.model.entity.App;
import com.wlinsk.service.manager.ManagerAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @Author: wlinsk
 * @Date: 2024/5/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerAppServiceImpl implements ManagerAppService {

    private final AppMapper appMapper;
    private final UserMapper userMapper;
    private final BasicTransactionTemplate basicTransactionTemplate;
    private final BusinessValidatorUtils businessValidatorUtils;

    @Override
    public void updateApp(ManagerUpdateAppReqDTO reqDTO) {
        if (AppTypeEnum.TEST.equals(reqDTO.getAppType())){
            throw new BasicException(SysCode.NOT_SUPPORT_TEST_APP);
        }
        if (ScoringStrategyEnum.AI_SCORE.equals(reqDTO.getScoringStrategy())){
            throw new BasicException(SysCode.NOT_SUPPORT_AI_SCORE);
        }
        App app = appMapper.queryByAppId(reqDTO.getAppId());
        Optional.ofNullable(app).orElseThrow(() -> new BasicException(SysCode.DATA_NOT_FOUND));
        App update = new App();
        BeanUtils.copyProperties(reqDTO,update);
        Date updateTime = new Date();
        update.setUpdateTime(updateTime);
        update.setVersion(app.getVersion());
        update.setReviewerId(BasicAuthContextUtils.getUserId());
        update.setReviewTime(updateTime);
        basicTransactionTemplate.execute(action -> {
            int result = appMapper.updateApp(update);
            if (result != 1){
                throw new BasicException(SysCode.DATABASE_UPDATE_ERROR);
            }
            return SysCode.success;
        });

    }

    @Override
    public void reviewApp(ManagerReviewAddReqDTO reqDTO) {
        App app = appMapper.queryByAppId(reqDTO.getAppId());
        Optional.ofNullable(app).orElseThrow(() -> new BasicException(SysCode.DATA_NOT_FOUND));
        if (app.getReviewStatus().equals(reqDTO.getReviewStatus())){
            throw new BasicException(SysCode.APP_REVIEW_STATUS_HAS_CHANGED);
        }
        String userId = BasicAuthContextUtils.getUserId();
        Date updateTime = new Date();
        App updateApp = new App();
        updateApp.setAppId(app.getAppId());
        updateApp.setVersion(app.getVersion());
        updateApp.setReviewerId(userId);
        updateApp.setUpdateTime(updateTime);
        updateApp.setReviewTime(updateTime);
        updateApp.setReviewStatus(reqDTO.getReviewStatus());
        updateApp.setReviewMessage(reqDTO.getReviewMessage());
        basicTransactionTemplate.execute(action -> {
            if (appMapper.updateApp(updateApp) != 1){
                throw new BasicException(SysCode.DATABASE_UPDATE_ERROR);
            }
            return SysCode.success;
        });
    }

    @Override
    public IPage<ManagerAppQueryPageRespDTO> queryPage(ManagerAppQueryPageReqDTO req) {
        Page<App> page = new Page<>(req.getPageNum(), req.getPageSize());
        IPage<App> iPage = appMapper.queryPage(page,req.getAppId(),req.getAppName());
        return iPage.convert(app -> {
            ManagerAppQueryPageRespDTO respDTO = new ManagerAppQueryPageRespDTO();
            BeanUtils.copyProperties(app, respDTO);
            return respDTO;
        });
    }

    @Override
    public void deleteById(String appId) {
        App app = appMapper.queryByAppId(appId);
        Optional.ofNullable(app).orElseThrow(() -> new BasicException(SysCode.DATA_NOT_FOUND));
        businessValidatorUtils.validateUserInfo(app.getUserId());
        App delete = new App();
        delete.setAppId(app.getAppId());
        delete.setVersion(app.getVersion());
        delete.setUpdateTime(new Date());
        delete.setDelState(app.getDelState());
        basicTransactionTemplate.execute(action -> {
            if (appMapper.deleteByAppId(delete) != 1){
                throw new BasicException(SysCode.DATABASE_DELETE_ERROR);
            }
            return SysCode.success;
        });
    }
}
