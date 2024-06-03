package com.wlinsk.model.dto.app.req;

import com.wlinsk.model.dto.IPageReq;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/6/3
 */
@Data
public class QueryMyAppPageReqDTO extends IPageReq implements Serializable {
    private static final long serialVersionUID = 5651355443071500639L;
    private String appName;
    private String appId;
}
