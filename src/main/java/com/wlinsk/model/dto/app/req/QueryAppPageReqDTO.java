package com.wlinsk.model.dto.app.req;

import com.wlinsk.model.dto.IPageReq;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/5/27
 */
@Data
public class QueryAppPageReqDTO extends IPageReq implements Serializable {
    private static final long serialVersionUID = 2640194203545166156L;
    private String context;
}
