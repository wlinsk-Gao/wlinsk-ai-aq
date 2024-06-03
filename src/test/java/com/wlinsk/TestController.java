package com.wlinsk;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlinsk.basic.enums.AppTypeEnum;
import com.wlinsk.basic.enums.ScoringStrategyEnum;
import com.wlinsk.basic.enums.UserRoleEnum;
import com.wlinsk.basic.exception.BasicException;
import com.wlinsk.basic.exception.SysCode;
import com.wlinsk.basic.idGenerator.IdUtils;
import com.wlinsk.basic.utils.RedisUtils;
import com.wlinsk.mapper.QuestionMapper;
import com.wlinsk.mapper.UserMapper;
import com.wlinsk.model.dto.question.QuestionContentDTO;
import com.wlinsk.model.dto.question.req.AiGenerateQuestionReqDTO;
import com.wlinsk.model.dto.question.resp.AiGenerateQuestionRespDTO;
import com.wlinsk.model.entity.App;
import com.wlinsk.model.entity.Question;
import com.wlinsk.model.entity.User;
import com.wlinsk.service.scoringHandler.ScoringStrategy;
import com.wlinsk.service.scoringHandler.ScoringStrategyFactory;
import com.wlinsk.service.user.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: wlinsk
 * @Date: 2024/5/22
 */
@Slf4j
@SpringBootTest
public class TestController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ScoringStrategyFactory scoringStrategyFactory;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private RedisUtils redisUtils;
    @Test
    public void testQuestionServiceAiGenerate(){
        AiGenerateQuestionReqDTO reqDTO = new AiGenerateQuestionReqDTO();
        reqDTO.setAppId("1800833126395904");
        reqDTO.setQuestionNumber(5);
        reqDTO.setOptionNumber(3);
        AiGenerateQuestionRespDTO result = questionService.aiGenerateQuestion(reqDTO);
        System.out.println();
    }
    @Test
    public void testQuestionInfo() {
        String appId = "1800833126395904";
        /*Question question = questionMapper.queryByAppId(appId);
        System.out.println();
        List<QuestionContentDTO> questionContent = question.getQuestionContent();*/
        String aiGenerateQuestionId = "aiGenerateQuestion3641500429619200";
        Question question = questionMapper.queryLatestQuestionContentByAppId(appId);
        List<String> oldQuestionList = new ArrayList<>();
        Set<String> deduplicateData = new HashSet<>();
        if (Objects.nonNull(question) && !CollectionUtils.isEmpty(question.getQuestionContent())){
            oldQuestionList = question.getQuestionContent().stream().map(QuestionContentDTO::getTitle).collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(aiGenerateQuestionId)){
            String val = redisUtils.getVal(aiGenerateQuestionId);
            if (StringUtils.isNotBlank(val)){
                /*oldQuestionList.addAll(JSONObject.parseArray(val, String.class));
                //去重集合
                deduplicateData.addAll(oldQuestionList);*/
                oldQuestionList.addAll(JSONObject.parseArray(val, String.class));
                oldQuestionList = oldQuestionList.stream().distinct().collect(Collectors.toList());
            }
        }
//        String oldQuestion = String.join(";", deduplicateData);
        String oldQuestion = String.join(";", oldQuestionList);
        System.out.println();

    }
    @Test
    public void testScoringStrategyFactory(){
        ScoringStrategy handler = scoringStrategyFactory.getHandler(ScoringStrategyEnum.AI_SCORE, AppTypeEnum.SCORE);
        handler.doScore(Arrays.asList(),new App());
    }
    @Test
    public void testUser(){
        String userId = "312612622053376";
        User user = userMapper.queryByUserId(userId);
        Optional.ofNullable(user)
                .filter(item -> !UserRoleEnum.BAN.equals(item.getUserRole()))
                .orElseThrow(() -> new BasicException(SysCode.DATA_NOT_FOUND));
        System.out.println();
    }
    @Test
    public void testUserPage(){
        Page<User> page = new Page<>(1, 10);
        IPage<User> iPage = userMapper.queryUserPage(page,null,null,null,null);
        List<User> records = iPage.getRecords();
        System.out.println();
    }
    @Test
    public void test1() {
        log.info("test1");
        for (int i = 0; i < 10; i++) {
            IdUtils.build(null);
        }
    }
}
