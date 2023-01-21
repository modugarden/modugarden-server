package com.modugarden.exception;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.status.enums.BaseResponseStatus;
import com.modugarden.exception.custom.BusinessException;
import com.modugarden.exception.custom.LoginCancelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponseDto<BaseResponseStatus> businessExceptionHandle(BusinessException e) {
        log.warn("businessException : {}", e);
        return new BaseResponseDto(e.getStatus());
    }

    @ExceptionHandler(LoginCancelException.class)
    public BaseResponseDto<BaseResponseStatus> loginCancelExceptionHandle(BusinessException e) {
        log.warn("loginCancelException : {}", e);
        return new BaseResponseDto(e.getStatus());
    }
}
