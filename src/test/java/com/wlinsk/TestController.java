package com.wlinsk;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlinsk.basic.enums.UserRoleEnum;
import com.wlinsk.basic.exception.BasicException;
import com.wlinsk.basic.exception.SysCode;
import com.wlinsk.basic.idGenerator.IdUtils;
import com.wlinsk.mapper.UserMapper;
import com.wlinsk.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

/**
 * @Author: wlinsk
 * @Date: 2024/5/22
 */
@Slf4j
@SpringBootTest
public class TestController {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void testUser(){
        String userId = "312612622053376";
        User user = userMapper.queryByUserId(userId);
        Optional.ofNullable(user)
                .filter(item -> !UserRoleEnum.BAN.equals(item.getUserRole()))
                .orElseThrow(() -> new BasicException(SysCode.DATA_NOT_FOUND));
        System.out.println();
    }
    @Test
    public void testUserPage(){
        Page<User> page = new Page<>(1, 10);
        IPage<User> iPage = userMapper.queryUserPage(page);
        List<User> records = iPage.getRecords();
        System.out.println();
    }
    @Test
    public void test1() {
        log.info("test1");
        for (int i = 0; i < 10; i++) {
            IdUtils.build(null);
        }
    }
}
