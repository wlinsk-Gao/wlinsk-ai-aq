package com.wlinsk.service.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wlinsk.basic.enums.UserRoleEnum;
import com.wlinsk.basic.exception.BasicException;
import com.wlinsk.basic.exception.SysCode;
import com.wlinsk.basic.idGenerator.IdUtils;
import com.wlinsk.basic.transaction.BasicTransactionTemplate;
import com.wlinsk.basic.utils.BasicAuthContextUtils;
import com.wlinsk.basic.utils.RedisUtils;
import com.wlinsk.mapper.UserMapper;
import com.wlinsk.model.dto.user.req.UserLoginReqDTO;
import com.wlinsk.model.dto.user.req.UserRegisterReqDTO;
import com.wlinsk.model.dto.user.resp.QueryUserDetailRespDTO;
import com.wlinsk.model.dto.user.resp.UserLoginRespDTO;
import com.wlinsk.model.entity.User;
import com.wlinsk.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private final String defaultAvatar = "https://wlinsk-ai-aq.oss-cn-guangzhou.aliyuncs.com/2024/05/26/73603796218521296026548255599116.png";
    private final UserMapper userMapper;
    private final RedisUtils redisUtils;
    private final BasicTransactionTemplate basicTransactionTemplate;

    @Override
    public void register(UserRegisterReqDTO dto) {
        if (Objects.nonNull(userMapper.queryByUserAccount(dto.getUserAccount()))){
            throw new BasicException(SysCode.USER_ACCOUNT_ALREADY_EXIST);
        }
        if (!dto.getUserPassword().equals(dto.getCheckPassword())){
            throw new BasicException(SysCode.USER_PASSWORD_ERROR);
        }
        User user = new User();
        user.init();
        user.setUserId(IdUtils.build(null));
        user.setUserName(dto.getUserAccount());
        user.setUserAccount(dto.getUserAccount());
        user.setUserPassword(dto.getUserPassword());
        user.setUserAvatar(defaultAvatar);
        basicTransactionTemplate.execute(action -> {
            if (userMapper.insert(user) != 1){
                throw new BasicException(SysCode.USER_REGISTER_ERROR);
            }
            return SysCode.success;
        });
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO dto) {
        User user = userMapper.queryByUserAccount(dto.getUserAccount());
        Optional.ofNullable(user)
                .orElseThrow(() -> new BasicException(SysCode.USER_ACCOUNT_NOT_EXIST));
        if (!user.getUserPassword().equals(dto.getUserPassword())){
            throw new BasicException(SysCode.USER_ACCOUNT_PASSWORD_ERROR);
        }
        if (UserRoleEnum.BAN.equals(user.getUserRole())){
            throw new BasicException(SysCode.USER_DISABLED);
        }
        String token = UUID.randomUUID().toString();
        String value = String.format("%s:%s",user.getUserId(),user.getUserRole().name());
        redisUtils.setVal(token,value,60*60*24);
        String oldToken = redisUtils.getVal(user.getUserId());
        if (StringUtils.isNotBlank(oldToken)){
            redisUtils.delVal(oldToken);
        }
        redisUtils.setVal(user.getUserId(),token,60*60*24);
        UserLoginRespDTO result = new UserLoginRespDTO();
        result.setToken(token);
        result.setUserId(user.getUserId());
        return result;
    }

    @Override
    public QueryUserDetailRespDTO queryById(String userId) {
        User user = userMapper.queryByUserId(userId);
        Optional.ofNullable(user)
                .orElseThrow(() -> new BasicException(SysCode.USER_ACCOUNT_NOT_EXIST));
        QueryUserDetailRespDTO result = new QueryUserDetailRespDTO();
        BeanUtils.copyProperties(user,result);
        return result;
    }

    @Override
    public void logout() {
        String userId = BasicAuthContextUtils.getUserId();
        if (StringUtils.isBlank(userId)){
            throw new BasicException(SysCode.SYSTEM_ERROE);
        }
        String token = redisUtils.getVal(userId);
        if (StringUtils.isBlank(token)){
            //未登录
            throw new BasicException(SysCode.SYS_TOKEN_EXPIRE);
        }
        redisUtils.delVal(token);
    }
}




