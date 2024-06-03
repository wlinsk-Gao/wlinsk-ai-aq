package com.wlinsk.service.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wlinsk.model.dto.app.req.ManagerAppQueryPageReqDTO;
import com.wlinsk.model.dto.app.req.ManagerReviewAddReqDTO;
import com.wlinsk.model.dto.app.req.ManagerUpdateAppReqDTO;
import com.wlinsk.model.dto.app.resp.ManagerAppQueryPageRespDTO;

/**
 * @Author: wlinsk
 * @Date: 2024/5/23
 */
public interface ManagerAppService {
    void updateApp(ManagerUpdateAppReqDTO reqDTO);

    void reviewApp(ManagerReviewAddReqDTO reqDTO);

    IPage<ManagerAppQueryPageRespDTO> queryPage(ManagerAppQueryPageReqDTO req);

    void deleteById(String appId);
}
