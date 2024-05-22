package com.wlinsk.basic.config.logInterceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: wlinsk
 * @Date: 2024/5/22
 */
@Slf4j
@Component
@WebFilter(urlPatterns = "/**", filterName = "bodyCacheFilter")
public class BodyCacheFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        BodyCacheHttpServletRequestWrapper requestWrapper = null;
        if ((request instanceof HttpServletRequest)  && response instanceof HttpServletResponse) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            // 如果不是 POST PATCH PUT 等有流的接口则无需进行类型转换
            if (!(request instanceof BodyCacheHttpServletRequestWrapper)) {
                requestWrapper = new BodyCacheHttpServletRequestWrapper(httpServletRequest);
            }
            if (!(response instanceof BodyCacheHttpServletResponseWrapper)) {
                response = new BodyCacheHttpServletResponseWrapper(httpServletResponse);
            }

        } else {
            log.warn("request no HttpServletRequest :{} or response no HttpServletResponse :{}", request, response);
        }
        if (requestWrapper != null) {
            chain.doFilter(requestWrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean hasStream(HttpServletRequest httpServletRequest) {
        String method = httpServletRequest.getMethod();
        return (HttpMethod.GET.matches(method) || HttpMethod.POST.matches(method) || HttpMethod.PATCH.matches(method) || HttpMethod.PUT.matches(method))
                && isSupportContentType(httpServletRequest.getContentType());
    }

    private List<String> getSupportContentType() {
        return Arrays.asList(MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE);
    }

    private boolean isSupportContentType(String contentType) {

        if (StringUtils.isEmpty(contentType)) {
            return false;
        }
        for (String supportContentType : getSupportContentType()) {
            if (contentType.contains(supportContentType)) {
                return true;
            }
        }
        return false;
    }
}
