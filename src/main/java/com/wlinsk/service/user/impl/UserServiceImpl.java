package com.wlinsk.service.user.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wlinsk.basic.bo.GiteeLoginCallbackBo;
import com.wlinsk.basic.bo.GiteeUserInfoBo;
import com.wlinsk.basic.enums.ThreePartLoginEnums;
import com.wlinsk.basic.enums.UserRoleEnum;
import com.wlinsk.basic.exception.BasicException;
import com.wlinsk.basic.exception.SysCode;
import com.wlinsk.basic.idGenerator.IdUtils;
import com.wlinsk.basic.transaction.BasicTransactionTemplate;
import com.wlinsk.basic.utils.BasicAuthContextUtils;
import com.wlinsk.basic.utils.RedisUtils;
import com.wlinsk.basic.utils.ThreePartLoginUtils;
import com.wlinsk.mapper.UserMapper;
import com.wlinsk.model.bo.TokenBo;
import com.wlinsk.model.dto.user.req.ThreePartLoginReqDTO;
import com.wlinsk.model.dto.user.req.UpdatePasswordReqDTO;
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

import java.util.Date;
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
    private final ThreePartLoginUtils threePartLoginUtils;

    @Override
    public void register(UserRegisterReqDTO dto) {
        if (Objects.nonNull(userMapper.queryByUserAccount(dto.getUserAccount()))){
            throw new BasicException(SysCode.USER_ACCOUNT_ALREADY_EXIST);
        }
        if (!dto.getUserPassword().equals(dto.getCheckPassword())){
            throw new BasicException(SysCode.USER_PASSWORD_ERROR);
        }
        registerUserToDB(dto.getUserAccount(), dto.getUserAccount(),dto.getUserPassword());
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
        return buildLoginResult(user,null);
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
        String oldTokenBo = redisUtils.getVal(userId);
        if (StringUtils.isBlank(oldTokenBo)){
            //未登录
            throw new BasicException(SysCode.SYS_TOKEN_EXPIRE);
        }
        TokenBo tokenBo = JSON.parseObject(oldTokenBo, TokenBo.class);
        redisUtils.delVal(tokenBo.getToken());
        redisUtils.delVal(userId);
    }

    @Override
    public String threePartLogin(ThreePartLoginReqDTO dto) {
        String result = threePartLoginUtils.doAuthorize(dto.getLoginType());
        Optional.ofNullable(result).orElseThrow(() -> new BasicException(SysCode.THREE_PART_LOGIN_ERROR));
        return result;
    }

    @Override
    public UserLoginRespDTO threePartLoginCallback(String code) {
        GiteeLoginCallbackBo callbackResult = threePartLoginUtils.getToken(code, null);
        GiteeUserInfoBo userInfo = threePartLoginUtils.getUserInfo(callbackResult.getAccess_token());
        String userAccount = String.format("%s_%d_%s",ThreePartLoginEnums.GITEE.getCode(),userInfo.getId(),userInfo.getName());
        User user = userMapper.queryByUserAccount(userAccount);
        if (Objects.isNull(user)){
            //注册
            String build = IdUtils.build(null);
            int length = build.length();
            String password = "gitee" + build.substring(length-8,length);
            user = registerUserToDB(userAccount,userInfo.getName(),password);
        }
        if (UserRoleEnum.BAN.equals(user.getUserRole())){
            throw new BasicException(SysCode.USER_DISABLED);
        }
        return buildLoginResult(user,callbackResult);
    }

    @Override
    public void updatePassword(UpdatePasswordReqDTO dto) {
        String userId = BasicAuthContextUtils.getUserId();
        User user = userMapper.queryByUserId(userId);
        if (!dto.getOriginalPassword().equals(user.getUserPassword())){
            throw new BasicException(SysCode.USER_PASSWORD_ERROR);
        }
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())){
            throw new BasicException(SysCode.USER_PASSWORD_NOT_SAME);
        }
        User update = new User();
        update.setUserId(userId);
        update.setUpdateTime(new Date());
        update.setUserPassword(dto.getNewPassword());
        update.setVersion(user.getVersion());
        basicTransactionTemplate.execute(action -> {
            int result = userMapper.updatePassword(update);
            if (result != 1){
                throw new BasicException(SysCode.DATABASE_UPDATE_ERROR);
            }
            return SysCode.success;
        });
    }

    private User registerUserToDB(String userAccount,String userName,String userPassword){
        User user = new User();
        user.init();
        user.setUserId(IdUtils.build(null));
        user.setUserName(userName);
        user.setUserAccount(userAccount);
        user.setUserPassword(userPassword);
        user.setUserAvatar(defaultAvatar);
        user.setUserRole(UserRoleEnum.USER);
        basicTransactionTemplate.execute(action -> {
            if (userMapper.insert(user) != 1){
                throw new BasicException(SysCode.USER_REGISTER_ERROR);
            }
            return SysCode.success;
        });
        return user;
    }

    private UserLoginRespDTO buildLoginResult(User user,GiteeLoginCallbackBo callbackBo){
        TokenBo tokenBo = new TokenBo();
        String token = UUID.randomUUID().toString();
        tokenBo.setToken(token);
        if (Objects.nonNull(callbackBo)){
            tokenBo.setThreePartAccessToken(callbackBo.getAccess_token());
            tokenBo.setThreePartRefreshToken(callbackBo.getRefresh_token());
        }
        String jsonTokenBo = JSON.toJSONString(tokenBo);
        String value = String.format("%s:%s",user.getUserId(),user.getUserRole().name());
        redisUtils.setVal(token,value,60*60*24);
        String oldJsonTokenBo = redisUtils.getVal(user.getUserId());
        if (StringUtils.isNotBlank(oldJsonTokenBo)){
            TokenBo oldTokenBo = JSON.parseObject(oldJsonTokenBo, TokenBo.class);
            redisUtils.delVal(oldTokenBo.getToken());
        }
        redisUtils.setVal(user.getUserId(),jsonTokenBo,60*60*24);
        UserLoginRespDTO result = new UserLoginRespDTO();
        result.setToken(token);
        result.setUserId(user.getUserId());
        return result;
    }
}




