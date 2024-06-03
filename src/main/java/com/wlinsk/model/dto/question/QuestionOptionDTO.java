package com.wlinsk.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/5/28
 */
@Data
public class QuestionOptionDTO implements Serializable {
    private static final long serialVersionUID = -739558462433757454L;
    /**
     * 结果
     */
    private String result;
    /**
     * 得分
     */
    private int score;
    /**
     * 选项值：
     */
    private String value;
    /**
     * 选项key：A，B，C，D
     */
    private String key;
}
