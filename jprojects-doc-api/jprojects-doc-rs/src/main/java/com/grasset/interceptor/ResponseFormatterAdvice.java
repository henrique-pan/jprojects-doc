package com.grasset.interceptor;

import com.grasset.dto.CollectionDTO;
import com.grasset.dto.HttpDTO;
import com.grasset.dto.ResponseDTO;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collection;

@RestControllerAdvice
public class ResponseFormatterAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof HttpDTO) {
            HttpDTO httpDTO = (HttpDTO) body;
            if (httpDTO.hasHeaders()) {
                httpDTO.getHeaders().forEach((headerName, headerValue) -> {
                    response.getHeaders().add(headerName, headerValue);
                });
            }

            HttpStatus httpStatus = HttpStatus.resolve(httpDTO.httpStatus);
            response.setStatusCode(httpStatus);

            if (body instanceof CollectionDTO) {
                CollectionDTO collectionDTO = (CollectionDTO) body;
                Collection collection = collectionDTO.getCollection();

                int size = collection.size();
                if(size <= 0 && httpDTO.httpStatus != HttpStatus.NO_CONTENT.value()) {
                    response.setStatusCode(HttpStatus.NO_CONTENT);
                }

                response.getHeaders().add("Total-Elements", String.valueOf(size));

                return new ResponseDTO(httpDTO.httpStatus, collection, httpDTO.apiMessage);
            }

            return new ResponseDTO(httpDTO.httpStatus, body, httpDTO.apiMessage);
        }

        if(body instanceof DefaultOAuth2AccessToken) {
            return body;
        }

        return new ResponseDTO(null, body, null);
    }

}
