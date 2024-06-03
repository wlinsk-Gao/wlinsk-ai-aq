package com.wlinsk.service.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wlinsk.model.dto.user.req.ManagerUserQueryPageReqDTO;
import com.wlinsk.model.dto.user.resp.ManagerUserQueryPageRespDTO;

/**
 * @Author: wlinsk
 * @Date: 2024/5/26
 */
public interface ManagerUserService {
    IPage<ManagerUserQueryPageRespDTO> queryPage(ManagerUserQueryPageReqDTO reqDTO);

    void deleteById(String userId);
}
