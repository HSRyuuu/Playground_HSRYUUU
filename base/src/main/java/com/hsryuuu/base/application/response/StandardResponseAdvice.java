package com.hsryuuu.base.application.response;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Optional;

//@RestControllerAdvice
public class StandardResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        String className = returnType.getDeclaringClass().getName();
        boolean isSwagger = className.startsWith("org.springdoc") || className.startsWith("springfox.");
        return !isSwagger;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // 파일일때
        if (selectedContentType.includes(MediaType.APPLICATION_OCTET_STREAM)) {
            return body;
        }

        // 컨트롤러의 return type 이 객체가 아닐 경우 처리 : 일단 그대로 반환한다.
        if (!MappingJackson2HttpMessageConverter.class.isAssignableFrom(selectedConverterType)) {
            return body;
        }

        // 에러 처리
        if (response instanceof ServletServerHttpResponse) {
            HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
            if (servletResponse.getStatus() != 200) {
                // 정상 에러
                if (body instanceof StandardResponse) {
                    return body;
                }
                // 예상치 못한 에러 (Spring 기본 에러 등)
                HttpStatus httpStatus = Optional.ofNullable(HttpStatus.resolve(servletResponse.getStatus())).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
                return new StandardResponse<>(httpStatus, body);
            }
        }

        // 정상
        if (body instanceof StandardResponse) {
            return body;
        } else {
            return new StandardResponse<>(body);
        }
    }
}
