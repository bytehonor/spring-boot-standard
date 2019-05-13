package com.bytehonor.sdk.server.spring.web.advisor;

import java.lang.annotation.Annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.bytehonor.sdk.server.spring.annotation.ResponseNotWrap;
import com.bytehonor.sdk.server.spring.config.SpringBootStandardProperties;
import com.bytehonor.sdk.server.spring.web.response.factory.ResponseStragetyFactory;
import com.bytehonor.sdk.server.spring.web.response.stragety.ResponseStragety;

@ControllerAdvice
public final class JsonResponseAdvisor implements ResponseBodyAdvice<Object> {

    @Autowired
    private SpringBootStandardProperties springBootStandardProperties;

    private static final Logger LOG = LoggerFactory.getLogger(JsonResponseAdvisor.class);

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return springBootStandardProperties.isResponseAdvisorEnable();
    }

    @Override
    public final Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("MediaType: {}", selectedContentType.toString());
        }
        if (MediaType.TEXT_HTML.equals(selectedContentType)) {
            return body;
        }
        for (Annotation ann : returnType.getMethodAnnotations()) {
            if (ann instanceof ResponseNotWrap) {
                LOG.info("method:{}, annotation:{}", returnType.getMethod().getName(), ann.getClass().getSimpleName());
                return body;
            }
        }
        ResponseStragety responseStragety = ResponseStragetyFactory.build(body, response, returnType,
                springBootStandardProperties);

        return responseStragety.process(request);
    }
}
