package com.wlinsk.model.convertor;

import com.alibaba.fastjson.JSONObject;
import com.wlinsk.model.dto.question.QuestionContentDTO;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: wlinsk
 * @Date: 2024/5/30
 */
public class QuestionConvertor {
    public static List<QuestionContentDTO> convertQuestionContent(List<QuestionContentDTO> questionContent){
        if (CollectionUtils.isEmpty(questionContent)){
            return Collections.emptyList();
        }
        List<QuestionContentDTO> result = new ArrayList<>();
        for (int i = 0; i < questionContent.size(); i++) {
            QuestionContentDTO contentDTO = JSONObject.toJavaObject((JSONObject) JSONObject.toJSON(questionContent.get(i)), QuestionContentDTO.class);
            result.add(contentDTO);
        }
        return result;
    }
}
