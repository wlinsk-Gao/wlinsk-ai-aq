package com.wlinsk.model.dto.question.req;

import com.wlinsk.model.dto.question.QuestionContentDTO;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: wlinsk
 * @Date: 2024/5/28
 */
@Data
public class UpdateQuestionReqDTO implements Serializable {
    private static final long serialVersionUID = 4862840053356475310L;
    @NotBlank(message = "题目id不可为空")
    private String questionId;
    @Valid
    @NotEmpty(message = "题目内容不可为空")
    private List<QuestionContentDTO> questionContent;
}
