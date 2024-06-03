package com.wlinsk.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wlinsk.model.dto.question.req.*;
import com.wlinsk.model.dto.question.resp.AiGenerateQuestionRespDTO;
import com.wlinsk.model.dto.question.resp.QueryQuestionPageRespDTO;
import com.wlinsk.model.entity.Question;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 *
 */
public interface QuestionService extends IService<Question> {

    void addQuestion(AddQuestionReqDTO reqDTO);

    IPage<QueryQuestionPageRespDTO> queryPage(QueryQuestionPageReqDTO reqDTO);

    void updateQuestion(UpdateQuestionReqDTO reqDTO);

    AiGenerateQuestionRespDTO aiGenerateQuestion(AiGenerateQuestionReqDTO reqDTO);

    SseEmitter aiGenerateSSE(AiGenerateQuestionSSEReqDTO reqDTO);
}
