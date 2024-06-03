package com.wlinsk.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wlinsk.basic.enums.AppTypeEnum;
import com.wlinsk.basic.exception.BasicException;
import com.wlinsk.basic.exception.SysCode;
import com.wlinsk.basic.idGenerator.IdUtils;
import com.wlinsk.basic.transaction.BasicTransactionTemplate;
import com.wlinsk.basic.utils.BasicAuthContextUtils;
import com.wlinsk.basic.utils.BusinessValidatorUtils;
import com.wlinsk.basic.utils.RedisUtils;
import com.wlinsk.basic.utils.zhiPuAI.AiUtils;
import com.wlinsk.mapper.QuestionMapper;
import com.wlinsk.mapper.UserMapper;
import com.wlinsk.model.dto.question.QuestionContentDTO;
import com.wlinsk.model.dto.question.QuestionContentSSEDTO;
import com.wlinsk.model.dto.question.req.*;
import com.wlinsk.model.dto.question.resp.AiGenerateQuestionRespDTO;
import com.wlinsk.model.dto.question.resp.QueryQuestionPageRespDTO;
import com.wlinsk.model.dto.user.resp.QueryUserDetailRespDTO;
import com.wlinsk.model.entity.App;
import com.wlinsk.model.entity.Question;
import com.wlinsk.model.entity.User;
import com.wlinsk.service.user.QuestionService;
import com.zhipu.oapi.service.v4.model.ModelData;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

    private final QuestionMapper questionMapper;
    private final UserMapper userMapper;
    private final BusinessValidatorUtils businessValidatorUtils;
    private final BasicTransactionTemplate basicTransactionTemplate;
    private final AiUtils aiUtils;
    private final RedisUtils redisUtils;

    @Override
    public void addQuestion(AddQuestionReqDTO reqDTO) {
        businessValidatorUtils.validateHandlerPermission(reqDTO.getAppId());
        Question question = new Question();
        question.init();
        question.setQuestionId(IdUtils.build(null));
        question.setUserId(BasicAuthContextUtils.getUserId());
        question.setQuestionContent(reqDTO.getQuestionContent());
        question.setAppId(reqDTO.getAppId());
        basicTransactionTemplate.execute(action -> {
            if (questionMapper.insert(question) != 1){
                throw new BasicException(SysCode.DATABASE_INSERT_ERROR);
            }
            return SysCode.success;
        });
    }

    @Override
    public IPage<QueryQuestionPageRespDTO> queryPage(QueryQuestionPageReqDTO reqDTO) {
        Page<Question> page = new Page<>(reqDTO.getPageNum(), reqDTO.getPageSize());
        IPage<Question> result = questionMapper.queryPage(page,reqDTO.getAppId());
        if (CollectionUtils.isEmpty(result.getRecords())){
            return result.convert(question -> null);
        }
        List<String> userIdList = result.getRecords().stream().map(Question::getUserId).distinct().collect(Collectors.toList());
        Map<String, User> userMap = userMapper.queryByUserIdList(userIdList).stream().filter(Objects::nonNull).collect(Collectors.toMap(User::getUserId, Function.identity()));
        return result.convert(question -> {
            QueryQuestionPageRespDTO respDTO = new QueryQuestionPageRespDTO();
            BeanUtils.copyProperties(question, respDTO);
            Optional.ofNullable(userMap.get(question.getUserId())).ifPresent(user -> {
                QueryUserDetailRespDTO userDTO = new QueryUserDetailRespDTO();
                BeanUtils.copyProperties(user, userDTO);
                respDTO.setUserInfo(userDTO);
            });
            return respDTO;
        });
    }

    @Override
    public void updateQuestion(UpdateQuestionReqDTO reqDTO) {
        Question question = questionMapper.queryById(reqDTO.getQuestionId());
        Optional.ofNullable(question).orElseThrow(() -> new BasicException(SysCode.DATA_NOT_FOUND));
        businessValidatorUtils.validateHandlerPermission(question.getAppId());
        Question update = new Question();
        update.setQuestionId(question.getQuestionId());
        update.setVersion(question.getVersion());
        update.setUpdateTime(new Date());
        update.setQuestionContent(reqDTO.getQuestionContent());
        basicTransactionTemplate.execute(action -> {
            if (questionMapper.updateQuestion(update) != 1){
                throw new BasicException(SysCode.DATABASE_UPDATE_ERROR);
            }
          return SysCode.success;
        });
    }

    @Override
    public AiGenerateQuestionRespDTO aiGenerateQuestion(AiGenerateQuestionReqDTO reqDTO) {
        App app = businessValidatorUtils.validateAppInfo(reqDTO.getAppId());
        businessValidatorUtils.validateUserInfo(app.getUserId());
        List<String> oldQuestionList = buildOldQuestionListForAIGenerate(reqDTO.getAppId(), reqDTO.getAiGenerateQuestionId());
        String oldQuestion = String.join(";", oldQuestionList);
        String prompt = getGenerateQuestionUserMessage(app, reqDTO.getQuestionNumber(), reqDTO.getOptionNumber(),oldQuestion);
        long startTime = System.currentTimeMillis();
        String result = aiUtils.doSyncRequest(AiUtils.GENERATE_QUESTION_SYSTEM_MESSAGE, prompt, null);
        log.info("ai生成耗时：{}毫秒",(System.currentTimeMillis() - startTime));
        // 截取需要的 JSON 信息
        int start = result.indexOf("[");
        int end = result.lastIndexOf("]");
        String json = result.substring(start, end + 1);
        log.info("ai生成结果（截取后的json）：{}",json);
        //当前最新的题目
        List<QuestionContentDTO> list = JSONObject.parseArray(json, QuestionContentDTO.class);
        String aiGenerateQuestionId = Optional.ofNullable(reqDTO.getAiGenerateQuestionId()).orElse(IdUtils.build("aiGenerateQuestion"));
        cacheQuestionTitle(list,oldQuestionList, aiGenerateQuestionId);
        AiGenerateQuestionRespDTO respDTO = new AiGenerateQuestionRespDTO();
        respDTO.setQuestionContent(list);
        respDTO.setAiGenerateQuestionId(aiGenerateQuestionId);
        return respDTO;
    }
    private void cacheQuestionTitle(List<QuestionContentDTO> newQuestionList,List<String> oldQuestionTitleList,String aiGenerateQuestionId){
        if (StringUtils.isBlank(aiGenerateQuestionId)){
            return;
        }
        List<String> newQuestionTitleList = newQuestionList.stream().map(QuestionContentDTO::getTitle).collect(Collectors.toList());
        newQuestionTitleList.addAll(oldQuestionTitleList);
        redisUtils.setVal(aiGenerateQuestionId,JSONObject.toJSONString(newQuestionList),3600);
    }
    private List<String> buildOldQuestionListForAIGenerate(String appId, String aiGenerateQuestionId){
        Question question = questionMapper.queryLatestQuestionContentByAppId(appId);
        List<String> oldQuestionList = new ArrayList<>();
        if (Objects.nonNull(question) && !CollectionUtils.isEmpty(question.getQuestionContent())){
            oldQuestionList = question.getQuestionContent().stream().map(QuestionContentDTO::getTitle).collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(aiGenerateQuestionId)){
            String val = redisUtils.getVal(aiGenerateQuestionId);
            if (StringUtils.isNotBlank(val)){
                oldQuestionList.addAll(JSONObject.parseArray(val, String.class));
                oldQuestionList = oldQuestionList.stream().distinct().collect(Collectors.toList());
            }
        }
        return oldQuestionList;
    }

    @Override
    public SseEmitter aiGenerateSSE(AiGenerateQuestionSSEReqDTO reqDTO) {
        if (StringUtils.isBlank(reqDTO.getAppId())){
            throw new BasicException(SysCode.APP_ID_NOT_EXIST);
        }
        if (Objects.isNull(reqDTO.getQuestionNumber())){
            throw new BasicException(SysCode.QUESTION_NUMBER_NOT_EXIST);
        }
        if (Objects.isNull(reqDTO.getOptionNumber())){
            throw new BasicException(SysCode.QUESTION_NUMBER_NOT_EXIST);
        }
        App app = businessValidatorUtils.validateAppInfo(reqDTO.getAppId());
        businessValidatorUtils.validateUserInfo(app.getUserId());
        List<String> oldQuestionTitleList = buildOldQuestionListForAIGenerate(reqDTO.getAppId(), reqDTO.getAiGenerateQuestionId());
        String oldQuestion = String.join(";", oldQuestionTitleList);
        String prompt = getGenerateQuestionUserMessage(app, reqDTO.getQuestionNumber(), reqDTO.getOptionNumber(),oldQuestion);
        String aiGenerateQuestionId = reqDTO.getAiGenerateQuestionId();
        if (StringUtils.isBlank(reqDTO.getAiGenerateQuestionId())){
            aiGenerateQuestionId = IdUtils.build("aiGenerateQuestion");
        }
        log.info("aiGenerateQuestionId: {}",aiGenerateQuestionId);
        //参数0是代表不超时
        SseEmitter sseEmitter = new SseEmitter(0L);
        long startTime = System.currentTimeMillis();
        Flowable<ModelData> modelDataFlowable = aiUtils.doStreamRequest(AiUtils.GENERATE_QUESTION_SYSTEM_MESSAGE, prompt, null);
        log.info("ai生成耗时：{}毫秒",(System.currentTimeMillis() - startTime));
        AtomicInteger count = new AtomicInteger(0);
        StringBuilder builder = new StringBuilder();
        List<QuestionContentDTO> newQuestionList = new ArrayList<>();
        String finalAiGenerateQuestionId = aiGenerateQuestionId;
        modelDataFlowable
                //指定线程池
                .observeOn(Schedulers.io())
                //初步收集数据
                .map(modelData -> modelData.getChoices().get(0).getDelta().getContent())
                //删除所有无效数据
                .map(content -> content.replaceAll("\\s",""))
                .filter(StringUtils::isNotBlank)
                .flatMap(content -> {
                    //把每道题目的json数据收集成每一个字符
                    List<Character> characterList = new ArrayList<>();
                    for (char c : content.toCharArray()) {
                        characterList.add(c);
                    }
                    return Flowable.fromIterable(characterList);
                })
                .doOnNext(character -> {
                    if (character == '{'){
                        count.incrementAndGet();
                    }
                    if (count.get() > 0){
                        //开始拼接字符
                        builder.append(character);
                    }
                    if (character == '}'){
                        count.decrementAndGet();
                        if (count.get() == 0){
                            String result = builder.toString();
                            log.info("ai流式生成题目：{}",result);
                            QuestionContentDTO dto = JSONObject.parseObject(result, QuestionContentDTO.class);
                            newQuestionList.add(dto);
                            //拼接完成，发送JSON数据给前端
                            sseEmitter.send(JSONObject.toJSONString(buildQuestionContentSSEDTO(dto,finalAiGenerateQuestionId)));
                            //重置builder
                            builder.setLength(0);
                        }
                    }
                }).doOnError(e -> log.error("ai流式生成题目异常：",e))
                //通知前端数据传输已完成
                .doOnComplete(() -> {
                    //缓存最新题目
                    cacheQuestionTitle(newQuestionList,oldQuestionTitleList, finalAiGenerateQuestionId);
                    sseEmitter.complete();
                })
                .subscribe();
        return sseEmitter;
    }

    private String getGenerateQuestionUserMessage(App app, int questionNumber, int optionNumber,String oldQuestions) {
        StringBuilder userMessage = new StringBuilder();
        userMessage.append(app.getAppName()).append("\n");
        userMessage.append(app.getAppDesc()).append("\n");
        userMessage.append(AppTypeEnum.getByCode(app.getAppType().getCode()).getMessage()).append("\n");
        userMessage.append(questionNumber).append("\n");
        userMessage.append(optionNumber);
        if (StringUtils.isNotBlank(oldQuestions)){
            userMessage.append("\n").append(oldQuestions);
        }
        return userMessage.toString();
    }
    private QuestionContentSSEDTO buildQuestionContentSSEDTO(QuestionContentDTO dto,String aiGenerateQuestionId){
        QuestionContentSSEDTO sseDTO = new QuestionContentSSEDTO();
        sseDTO.setTitle(dto.getTitle());
        sseDTO.setOptions(dto.getOptions());
        sseDTO.setAiGenerateQuestionId(aiGenerateQuestionId);
        log.info("aiGenerateQuestionId: {}",aiGenerateQuestionId);
        return sseDTO;
    }
}




