package com.wlinsk.model.dto.question.resp;

import com.wlinsk.model.dto.question.QuestionContentDTO;
import com.wlinsk.model.dto.user.resp.QueryUserDetailRespDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: wlinsk
 * @Date: 2024/5/28
 */
@Data
public class QueryQuestionPageRespDTO implements Serializable {
    private static final long serialVersionUID = -3638471131964446050L;
    private String questionId;
    private List<QuestionContentDTO> questionContent;
    private String appId;
    private String userId;
    private Date createTime;
    private Date updateTime;
    private QueryUserDetailRespDTO userInfo;
}
