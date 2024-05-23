package com.wlinsk.basic.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wlinsk.basic.serializer.JackSonDeserializer;
import com.wlinsk.basic.serializer.JackSonSerializer;

/**
 * @Author: wlinsk
 * @Date: 2024/5/23
 */
@JsonSerialize(using = JackSonSerializer.class)
@JsonDeserialize(using = JackSonDeserializer.class)
public enum AppTypeEnum implements BaseEnum<AppTypeEnum,Integer>{
    //（0-得分类，1-测评类）
    SCORE(0,"得分类"),
    TEST(1,"测评类"),
    ;
    private final Integer code;

    private final String message;

    AppTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
