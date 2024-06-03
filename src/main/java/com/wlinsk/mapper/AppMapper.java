package com.wlinsk.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlinsk.model.entity.App;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity com.wlinsk.model.entity.App
 */
public interface AppMapper extends BaseMapper<App> {

    App queryByAppId(@Param("appId") String appId);

    int deleteByAppId(@Param("app") App app);

    int updateApp(@Param("app") App app);

    IPage<App> queryPage(Page<App> page,@Param("appId") String appId,@Param("appName") String appName);

    IPage<App> queryPageForClient(Page<App> page, @Param("context") String context);

    IPage<App> queryMyPageForClient(Page<App> page, @Param("userId")String userId, @Param("appId")String appId, @Param("appName")String appName);
}




