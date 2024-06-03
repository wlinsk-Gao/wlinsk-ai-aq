package com.wlinsk.model.dto.answer.req;

import com.wlinsk.model.dto.IPageReq;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/5/30
 */
@Data
public class QueryUserAnswerPageReqDTO extends IPageReq implements Serializable {
    private static final long serialVersionUID = -8010096221751272724L;
    private String appId;
    private String recordId;
    private String resultName;
}
