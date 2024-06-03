package com.wlinsk.service.user.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wlinsk.basic.enums.AppTypeEnum;
import com.wlinsk.basic.enums.DelStateEnum;
import com.wlinsk.basic.exception.BasicException;
import com.wlinsk.basic.exception.SysCode;
import com.wlinsk.basic.idGenerator.IdUtils;
import com.wlinsk.basic.transaction.BasicTransactionTemplate;
import com.wlinsk.basic.utils.BasicAuthContextUtils;
import com.wlinsk.basic.utils.BusinessValidatorUtils;
import com.wlinsk.mapper.ScoringResultMapper;
import com.wlinsk.mapper.UserMapper;
import com.wlinsk.model.dto.scoringResult.req.AddScoringResultReqDTO;
import com.wlinsk.model.dto.scoringResult.req.QueryScoringResultPageReqDTO;
import com.wlinsk.model.dto.scoringResult.req.UpdateScoringResultReqDTO;
import com.wlinsk.model.dto.scoringResult.resp.QueryScoringResultPageRespDTO;
import com.wlinsk.model.dto.user.resp.QueryUserDetailRespDTO;
import com.wlinsk.model.entity.App;
import com.wlinsk.model.entity.ScoringResult;
import com.wlinsk.model.entity.User;
import com.wlinsk.service.user.ScoringResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringResultServiceImpl extends ServiceImpl<ScoringResultMapper, ScoringResult>
    implements ScoringResultService{

    private final ScoringResultMapper scoringResultMapper;
    private final UserMapper userMapper;
    private final BusinessValidatorUtils businessValidatorUtils;
    private final BasicTransactionTemplate basicTransactionTemplate;
    @Override
    public void addScoringResult(AddScoringResultReqDTO reqDTO) {
        validateForAddAndUpdate(reqDTO.getAppId(), reqDTO.getResultProp(), reqDTO.getResultScoreRange());
        ScoringResult scoringResult = new ScoringResult();
        BeanUtils.copyProperties(reqDTO,scoringResult);
        scoringResult.init();
        scoringResult.setResultId(IdUtils.build(null));
        scoringResult.setUserId(BasicAuthContextUtils.getUserId());
        basicTransactionTemplate.execute(action -> {
            if (scoringResultMapper.insert(scoringResult) != 1){
                throw new BasicException(SysCode.DATABASE_INSERT_ERROR);
            }
            return SysCode.success;
        });
    }

    @Override
    public void updateScoringResult(UpdateScoringResultReqDTO reqDTO) {
        ScoringResult scoringResult = scoringResultMapper.queryById(reqDTO.getResultId());
        Optional.ofNullable(scoringResult).orElseThrow(() -> new BasicException(SysCode.DATA_NOT_FOUND));
        if (!scoringResult.getAppId().equals(reqDTO.getAppId())){
            throw new BasicException(SysCode.DATA_NOT_FOUND);
        }
        validateForAddAndUpdate(reqDTO.getAppId(), reqDTO.getResultProp(), reqDTO.getResultScoreRange());
        ScoringResult update = new ScoringResult();
        BeanUtils.copyProperties(reqDTO,update);
        update.setVersion(scoringResult.getVersion());
        update.setUpdateTime(new Date());
        basicTransactionTemplate.execute(action -> {
            if (scoringResultMapper.updateScoringResult(update) != 1) {
                throw new BasicException(SysCode.DATABASE_UPDATE_ERROR);
            }
            return SysCode.success;
        });
    }

    @Override
    public IPage<QueryScoringResultPageRespDTO> queryPage(QueryScoringResultPageReqDTO reqDTO) {
        Page<ScoringResult> page = new Page<>(reqDTO.getPageNum(), reqDTO.getPageSize());
        IPage<ScoringResult> iPage = scoringResultMapper.queryPage(page,reqDTO.getAppId(),reqDTO.getResultName(),reqDTO.getResultDesc());
        if (CollectionUtils.isEmpty(iPage.getRecords())){
            return iPage.convert(scoringResult -> null);
        }
        List<String> userIdList = iPage.getRecords().stream().map(ScoringResult::getUserId).distinct().collect(Collectors.toList());
        Map<String, User> userMap = userMapper.queryByUserIdList(userIdList).stream().filter(Objects::nonNull).collect(Collectors.toMap(User::getUserId, Function.identity()));
        return iPage.convert(scoringResult -> {
            QueryScoringResultPageRespDTO respDTO = new QueryScoringResultPageRespDTO();
            BeanUtils.copyProperties(scoringResult,respDTO);
            Optional.ofNullable(userMap.get(scoringResult.getUserId())).ifPresent(user -> {
                QueryUserDetailRespDTO userDTO = new QueryUserDetailRespDTO();
                BeanUtils.copyProperties(user, userDTO);
                respDTO.setUserInfo(userDTO);
            });
            return respDTO;
        });
    }

    @Override
    public void deleteById(String resultId) {
        ScoringResult scoringResult = scoringResultMapper.queryById(resultId);
        Optional.ofNullable(scoringResult).orElseThrow(() -> new BasicException(SysCode.DATA_NOT_FOUND));
        businessValidatorUtils.validateHandlerPermission(scoringResult.getAppId());
        ScoringResult delete = new ScoringResult();
        delete.setResultId(scoringResult.getResultId());
        delete.setVersion(scoringResult.getVersion());
        delete.setUpdateTime(new Date());
        delete.setDelState(DelStateEnum.DEL);
        basicTransactionTemplate.execute(action -> {
            if (scoringResultMapper.deleteScoringResult(scoringResult) != 1){
                throw new BasicException(SysCode.DATABASE_DELETE_ERROR);
            }
            return SysCode.success;
        });
    }

    private void validateForAddAndUpdate(String appId, List<String> resultProp,Integer resultScoreRange) {
        App app = businessValidatorUtils.validateAppInfo(appId);
        businessValidatorUtils.validateUserInfo(app.getUserId());
        if (AppTypeEnum.SCORE.equals(app.getAppType()) && Objects.isNull(resultScoreRange)){
            throw new BasicException(SysCode.SCORING_RESULT_RANGE_IS_NULL);
        }
        if (AppTypeEnum.TEST.equals(app.getAppType()) && CollectionUtils.isEmpty(resultProp)){
            throw new BasicException(SysCode.SCORING_PROP_IS_NULL);
        }
    }
}




