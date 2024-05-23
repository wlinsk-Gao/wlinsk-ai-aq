package com.wlinsk.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/5/23
 */
@Data
public class IPageReq implements Serializable {
    private static final long serialVersionUID = -4387737655867948406L;
    /**
     * 页数
     */
    @NotNull(message = "pageNum can not be null")
    private Integer pageNum;
    /**
     * 页面数据条数
     */
    @NotNull(message = "pageSize can not be null")
    private Integer pageSize;
}
