package com.wlinsk.basic.utils.zhiPuAI;

import com.wlinsk.basic.exception.BasicException;
import com.wlinsk.basic.exception.SysCode;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wlinsk
 * @Date: 2024/5/31
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AiUtils {
    private final ClientV4 clientV4;
    /**
     * 稳定的随机数
     */
    private static final float STABLE_TEMPERATURE = 0.05f;

    /**
     * 不稳定的随机数
     */
    private static final float UNSTABLE_TEMPERATURE = 0.99f;

    public static final String GENERATE_QUESTION_SYSTEM_MESSAGE =
            "你是一位严谨的出题专家，我会给你如下信息：\n" +
                    "```\n" +
                    "应用名称，\n" +
                    "【【【应用描述】】】，\n" +
                    "应用类别，\n" +
                    "要生成的题目数，\n" +
                    "每个题目的选项数，\n" +
                    "【【【已存在的题目】】】\n" +
                    "```\n" +
                    "\n" +
                    "请你根据上述信息，按照以下步骤来出题：\n" +
                    "1. 要求：题目和选项尽可能地短，题目不要包含序号，选项内容不要包含key，每个题目的选项数以我输入的选项数为最大值，题目不能重复，如果我有提供已存在的题目，那么同样不能跟已存在的题目重复，更重要的是输出的结果中不要出现Java语言中JSON反序列化失败的格式。\n" +
                    "2. 严格按照下面的 json 格式输出题目和选项：\n" +
                    "3. 如果应用类别为得分类应用，则每道题目的正确选项的score字段固定为5，其他选项的score固定为0；\n" +
                    "```\n" +
                    "[{\"options\":[{\"value\":\"选项内容\",\"key\":\"A\",\"score\":\"5\",\"result\":\"\"},{\"value\":\"\",\"key\":\"B\",\"score\":\"0\",\"result\":\"\"}],\"title\":\"题目标题\"}]\n" +
                    "```\n" +
                    "title 是题目，options 是选项，每个选项的 key 按照英文字母序（比如 A、B、C、D）以此类推，value 是选项内容；\n" +
                    "3. 检查题目是否包含序号，若包含序号则去除序号；\n" +
                    "4. 返回的题目列表格式必须为 JSON 数组；";

    /**
     * 同步请求（答案不稳定）
     *
     * @param systemMessage
     * @param userMessage
     * @return
     */
    public String doSyncUnstableRequest(String systemMessage, String userMessage) {
        return doRequest(systemMessage, userMessage, Boolean.FALSE, UNSTABLE_TEMPERATURE);
    }

    /**
     * 同步请求（答案较稳定）
     *
     * @param systemMessage
     * @param userMessage
     * @return
     */
    public String doSyncStableRequest(String systemMessage, String userMessage) {
        return doRequest(systemMessage, userMessage, Boolean.FALSE, STABLE_TEMPERATURE);
    }

    /**
     * 同步请求
     *
     * @param systemMessage
     * @param userMessage
     * @param temperature
     * @return
     */
    public String doSyncRequest(String systemMessage, String userMessage, Float temperature) {
        return doRequest(systemMessage, userMessage, Boolean.FALSE, temperature);
    }

    /**
     * 通用请求（简化消息传递）
     *
     * @param systemMessage
     * @param userMessage
     * @param stream
     * @param temperature
     * @return
     */
    public String doRequest(String systemMessage, String userMessage, Boolean stream, Float temperature) {
        List<ChatMessage> chatMessageList = buildChatMessageList(systemMessage, userMessage);
        return doRequest(chatMessageList, stream, temperature);
    }

    /**
     * 通用流式请求（简化消息传递）
     * @param systemMessage
     * @param userMessage
     * @param temperature
     * @return
     */
    public Flowable<ModelData> doStreamRequest(String systemMessage, String userMessage, Float temperature){
        List<ChatMessage> chatMessageList = buildChatMessageList(systemMessage, userMessage);
        return doStreamRequest(chatMessageList,temperature);
    }

    private List<ChatMessage> buildChatMessageList(String systemMessage, String userMessage) {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        chatMessageList.add(systemChatMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        chatMessageList.add(userChatMessage);
        return chatMessageList;
    }

    /**
     * 通用流式请求
     * @param messages
     * @param temperature
     * @return
     */
    public Flowable<ModelData> doStreamRequest(List<ChatMessage> messages,Float temperature){
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.TRUE)
                .temperature(temperature)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();
        try {
            ModelApiResponse modelApiResponse = clientV4.invokeModelApi(completionRequest);
            return modelApiResponse.getFlowable();
        } catch (Exception e) {
            log.error("请求智谱接口异常： ", e);
            throw new BasicException(SysCode.SYSTEM_ERROE);
        }
    }

    /**
     * 通用请求
     *
     * @param messages
     * @param stream
     * @param temperature
     * @return
     */
    public String doRequest(List<ChatMessage> messages, Boolean stream, Float temperature) {
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(stream)
                .temperature(temperature)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();
        try {
            ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
            return invokeModelApiResp.getData().getChoices().get(0).toString();
        } catch (Exception e) {
            log.error("请求智谱接口异常： ", e);
            throw new BasicException(SysCode.SYSTEM_ERROE);
        }
    }
}
