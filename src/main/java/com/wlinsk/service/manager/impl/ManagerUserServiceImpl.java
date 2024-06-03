package com.wlinsk.service.manager.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlinsk.basic.enums.DelStateEnum;
import com.wlinsk.basic.enums.UserRoleEnum;
import com.wlinsk.basic.exception.BasicException;
import com.wlinsk.basic.exception.SysCode;
import com.wlinsk.basic.transaction.BasicTransactionTemplate;
import com.wlinsk.basic.utils.BasicAuthContextUtils;
import com.wlinsk.basic.utils.RedisUtils;
import com.wlinsk.mapper.UserMapper;
import com.wlinsk.model.dto.user.req.ManagerUserQueryPageReqDTO;
import com.wlinsk.model.dto.user.resp.ManagerUserQueryPageRespDTO;
import com.wlinsk.model.entity.User;
import com.wlinsk.service.manager.ManagerUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author: wlinsk
 * @Date: 2024/5/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerUserServiceImpl implements ManagerUserService {
    private final UserMapper userMapper;
    private final BasicTransactionTemplate basicTransactionTemplate;
    private final RedisUtils redisUtils;
    @Override
    public IPage<ManagerUserQueryPageRespDTO> queryPage(ManagerUserQueryPageReqDTO reqDTO) {
        Page<User> page = new Page<>(reqDTO.getPageNum(), reqDTO.getPageSize());
        IPage<User> iPage = userMapper.queryUserPage(page, reqDTO.getUserId(), reqDTO.getUserAccount(),
                reqDTO.getUserName(), reqDTO.getUserRole());
        return iPage.convert(user -> {
            ManagerUserQueryPageRespDTO dto = new ManagerUserQueryPageRespDTO();
            BeanUtils.copyProperties(user, dto);
            return dto;
        });
    }

    @Override
    public void deleteById(String userId) {
        User targetUser = userMapper.queryByUserId(userId);
        Optional.ofNullable(targetUser).orElseThrow(() -> new BasicException(SysCode.DATA_NOT_FOUND));
        User user = userMapper.queryByUserId(BasicAuthContextUtils.getUserId());
        if (Objects.isNull(user) || !Objects.equals(user.getUserRole(), UserRoleEnum.ADMIN)) {
            throw new BasicException(SysCode.SYSTEM_NO_PERMISSION);
        }
        User delete = new User();
        delete.setUserId(targetUser.getUserId());
        delete.setVersion(targetUser.getVersion());
        delete.setUpdateTime(new Date());
        delete.setDelState(DelStateEnum.DEL);
        basicTransactionTemplate.execute(action -> {
            if (userMapper.deleteUser(delete) != 1){
                throw new BasicException(SysCode.DATABASE_DELETE_ERROR);
            }
            String val = redisUtils.getVal(targetUser.getUserId());
            if (StringUtils.isNotBlank(val)){
                redisUtils.delVal(val);
            }
            return SysCode.success;
        });


    }
}
