package com.wlinsk.model.dto.user.req;

import com.wlinsk.basic.enums.ThreePartLoginEnums;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: wlinsk
 * @Date: 2024/8/26
 */
@Data
public class ThreePartLoginReqDTO implements Serializable {
    private static final long serialVersionUID = -8435026622142289277L;

    @NotNull(message = "第三方渠道不可为空")
    private ThreePartLoginEnums loginType;

}
