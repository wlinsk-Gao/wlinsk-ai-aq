package com.wlinsk.service.user;

import com.wlinsk.model.dto.user.req.UserLoginReqDTO;
import com.wlinsk.model.dto.user.req.UserRegisterReqDTO;
import com.wlinsk.model.dto.user.resp.QueryUserDetailRespDTO;
import com.wlinsk.model.dto.user.resp.UserLoginRespDTO;
import com.wlinsk.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface UserService extends IService<User> {

    void register(UserRegisterReqDTO dto);

    UserLoginRespDTO login(UserLoginReqDTO dto);

    QueryUserDetailRespDTO queryById(String userId);

    void logout();

}
