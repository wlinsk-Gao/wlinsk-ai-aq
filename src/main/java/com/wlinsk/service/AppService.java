package com.wlinsk.service;

import com.wlinsk.model.dto.app.req.AddAppReqDTO;
import com.wlinsk.model.dto.app.req.DeleteAppReqDTO;
import com.wlinsk.model.entity.App;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface AppService extends IService<App> {

    String addApp(AddAppReqDTO dto);

    void deleteApp(DeleteAppReqDTO reqDTO);
}
