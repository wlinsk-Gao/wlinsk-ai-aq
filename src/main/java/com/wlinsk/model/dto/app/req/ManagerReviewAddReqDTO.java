package com.wlinsk.model.dto.app.req;

import com.wlinsk.basic.enums.ReviewStatusEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/5/23
 */
@Data
public class ManagerReviewAddReqDTO implements Serializable {
    private static final long serialVersionUID = -748609130083070020L;
    /**
     * 应用id
     */
    @NotBlank(message = "应用id不能为空")
    private String appId;
    /**
     * 审核状态：0-待审核, 1-通过, 2-拒绝
     */
    @NotNull(message = "审核状态不能为空")
    private ReviewStatusEnum reviewStatus;
    /**
     * 审核信息
     */
    private String reviewMessage;
}
