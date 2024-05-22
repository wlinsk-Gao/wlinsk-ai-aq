package com.wlinsk.basic.config.swagger;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.service.Header;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.VendorExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: wlinsk
 * @Date: 2024/5/22
 */
@EqualsAndHashCode(callSuper = true)
public class BasicResponseMessage extends ResponseMessage {
    @Getter
    private final String rspCd;

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(1000);


    public BasicResponseMessage(String code, String message, ModelReference responseModel, Map<String, Header> headers, List<VendorExtension> vendorExtensions) {
        super(getNumber(), message, responseModel, headers, vendorExtensions);
        rspCd = code;
    }

    public BasicResponseMessage(ApiCode apiError){
        super(getNumber(),apiError.message(),null, Collections.emptyMap(), Collections.emptyList());
        rspCd = apiError.code();
    }


    private static int getNumber(){
        if(ATOMIC_INTEGER.intValue() > 2000){
            ATOMIC_INTEGER.set(1000);
        }
        return ATOMIC_INTEGER.getAndIncrement();
    }
}
