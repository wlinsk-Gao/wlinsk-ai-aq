package com.wlinsk.basic.config.swagger;

import io.swagger.models.Response;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2MapperImpl;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: wlinsk
 * @Date: 2024/5/22
 */
public class BasicModelToSwagger2Mapper extends ServiceModelToSwagger2MapperImpl {
    @Override
    protected Map<String, Response> mapResponseMessages(Set<ResponseMessage> from) {
        Map<Integer, BasicResponseMessage> camResponseMessageMap = from.stream()
                .filter(BasicResponseMessage.class::isInstance).map(BasicResponseMessage.class::cast)
                .collect(Collectors.toMap(BasicResponseMessage::getCode, Function.identity()));
        Map<String, Response> stringResponseMap = super.mapResponseMessages(from);
        stringResponseMap = stringResponseMap.entrySet().stream()
                .filter(entry -> Integer.parseInt(entry.getKey()) >= 1000)
                .collect(Collectors.toMap(entry -> camResponseMessageMap.get(Integer.valueOf(entry.getKey())).getRspCd(), Map.Entry::getValue));
        return stringResponseMap;
    }
}
