package com.wlinsk.basic.config.swagger;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

import java.util.*;

import static springfox.documentation.schema.ResolvedTypes.modelRefFactory;

/**
 * @Author: wlinsk
 * @Date: 2024/5/22
 */
public class BasicOperationBuilderPlugin implements OperationBuilderPlugin {
    @Autowired
    private TypeNameExtractor typeNameExtractor;

    @Override
    public void apply(OperationContext context) {
        Optional<ApiCodes> annotation = context.findAnnotation(ApiCodes.class);
        HashSet<ResponseMessage> objects = new HashSet<>();
        ResolvedType returnType = context.getReturnType();
        ModelReference modelRef;
        ModelContext modelContext = ModelContext.returnValue(
                context.getGroupName(),
                returnType,
                context.getDocumentationType(),
                context.getAlternateTypeProvider(),
                context.getGenericsNamingStrategy(),
                context.getIgnorableParameterTypes());
        modelRef = modelRefFactory(modelContext, typeNameExtractor).apply(returnType);
        objects.add(new BasicResponseMessage("200", "success", modelRef, new HashMap<>(), Collections.emptyList()));
        ApiCodes apiCodes = annotation.orNull();
        if (apiCodes != null) {
            List<ApiCode> apiCodeList = Arrays.asList(apiCodes.value());
            apiCodeList.stream()
                    .filter(error -> !"200".equals(error.code())).map(BasicResponseMessage::new)
                    .forEach(objects::add);
        }
        context.operationBuilder().responseMessages(objects);
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }

}
