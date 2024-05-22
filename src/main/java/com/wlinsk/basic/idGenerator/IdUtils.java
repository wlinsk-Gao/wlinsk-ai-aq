package com.wlinsk.basic.idGenerator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

@Slf4j
public class IdUtils {

    private final static IdGenerator sequenceGenerator = new IdGenerator(RandomUtils.nextInt(1,10),
            RandomUtils.nextInt(1,10));
    public static String build(String prefix){
        String id = sequenceGenerator.nextSeq();
        String result = prefix==null?id:prefix+id;
        log.info("the id is:{}",result);
        return result;
    }
}
