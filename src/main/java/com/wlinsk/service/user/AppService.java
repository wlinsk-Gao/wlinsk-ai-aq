package com.wlinsk.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wlinsk.model.dto.app.req.AddAppReqDTO;
import com.wlinsk.model.dto.app.req.DeleteAppReqDTO;
import com.wlinsk.model.dto.app.req.QueryAppPageReqDTO;
import com.wlinsk.model.dto.app.req.UpdateAppReqDTO;
import com.wlinsk.model.dto.app.resp.QueryAppDetailsRespDTO;
import com.wlinsk.model.dto.app.resp.QueryAppPageRespDTO;
import com.wlinsk.model.entity.App;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface AppService extends IService<App> {

    String addApp(AddAppReqDTO dto);

    void deleteApp(DeleteAppReqDTO reqDTO);

    IPage<QueryAppPageRespDTO> queryPage(QueryAppPageReqDTO reqDTO);

    QueryAppDetailsRespDTO queryById(String appId);

    void updateApp(UpdateAppReqDTO reqDTO);
}
