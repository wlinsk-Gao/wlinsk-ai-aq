package com.wlinsk.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlinsk.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity com.wlinsk.model.entity.User
 */
public interface UserMapper extends BaseMapper<User> {

    User queryByUserAccount(@Param("userAccount") String userAccount);

    User queryByUserId(@Param("userId") String userId);

    IPage<User> queryUserPage(Page<User> page);
}




