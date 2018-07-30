package com.henrique.interceptor;

import com.henrique.exception.RequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

@Slf4j
@Component
public class LogIdInterceptor extends HandlerInterceptorAdapter {

    private static final Integer RANDOM_NUMERIC = 20;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String logId = RandomStringUtils.randomNumeric(RANDOM_NUMERIC);
            MDC.put("logId", logId);
            return true;
        } catch (Exception e) {
            printRequestDetails(request);
            log.error("Error on preHandle: ", e);
            throw new RequestException("Error on preHandle: " + e.getMessage());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            MDC.remove("logId");
        } catch (Exception e) {
            printRequestDetails(request);
            log.error("Error on afterCompletion: ", e);
            throw new RequestException("Error on afterCompletion: " + e.getMessage());
        }
    }

    private void printRequestDetails(ServletRequest request) {
        log.info("client_IP:" + request.getRemoteAddr());

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            // Request URL
            log.info("Request: " + httpRequest.getRequestURL().toString());
            // Parameters
            Map<String, String[]> parameterMap = httpRequest.getParameterMap();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                log.info("Parameter " + entry.getKey() + "=" + Arrays.toString(entry.getValue()));
            }
            // Headers
            Enumeration<String> headerNames = httpRequest.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                Enumeration<String> headers = httpRequest.getHeaders(headerName);
                while (headers.hasMoreElements()) {
                    String headerValue = headers.nextElement();
                    log.info("Header " + headerName + "=" + headerValue);
                }
            }
        }
    }

}
