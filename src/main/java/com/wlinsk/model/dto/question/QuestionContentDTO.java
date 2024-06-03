package com.wlinsk.model.dto.question;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: wlinsk
 * @Date: 2024/5/28
 */
@Data
public class QuestionContentDTO implements Serializable {
    private static final long serialVersionUID = -407413857539800612L;
    /**
     * 题目标题
     */
    @NotBlank(message = "题目标题不能为空")
    private String title;

    /**
     * 题目选项列表
     */
    @NotEmpty(message = "题目选项列表不能为空")
    private List<QuestionOptionDTO> options;

}
