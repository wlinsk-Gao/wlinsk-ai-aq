package com.wlinsk;

import com.wlinsk.basic.idGenerator.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: wlinsk
 * @Date: 2024/5/22
 */
@Slf4j
@SpringBootTest
public class TestController {
    @Test
    public void test1() {
        log.info("test1");
        for (int i = 0; i < 10; i++) {
            IdUtils.build(null);
        }
    }
}
