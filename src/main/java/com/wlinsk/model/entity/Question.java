package com.wlinsk.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 题目表
 * @TableName tb_question
 */
@TableName(value ="tb_question",autoResultMap = true)
@Data
public class Question extends BaseEntity implements Serializable {
    /**
     * 题目id
     */
    private String questionId;

    /**
     * 题目内容（json格式）
     */
    private String questionContent;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 用户id
     */
    private String userId;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}