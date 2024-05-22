package com.wlinsk.basic.enums;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wlinsk.basic.serializer.JackSonDeserializer;
import com.wlinsk.basic.serializer.JackSonSerializer;

/**
 * @Author: wlinsk
 * @Date: 2024/4/21
 */
@JsonSerialize(using = JackSonSerializer.class)
@JsonDeserialize(using = JackSonDeserializer.class)
public enum DelStateEnum implements BaseEnum<DelStateEnum, Integer> {

    /**
     * 正常
     */
    NORMAL(0, "正常"),

    /**
     * 软删除
     */
    DEL(1, "软删除"),
    ;

    private Integer code;

    private String message;

    DelStateEnum(Integer code, String message) {
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
