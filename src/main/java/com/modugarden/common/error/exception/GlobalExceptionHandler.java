package com.modugarden.common.error.exception;

import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.common.error.exception.custom.LoginCancelException;
import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.error.enums.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponseDto<ErrorMessage> businessExceptionHandle(BusinessException e) {
        log.warn("businessException : {}", e);
        return new BaseResponseDto(e.getErrorMessage());
    }

    @ExceptionHandler(LoginCancelException.class)
    public BaseResponseDto<ErrorMessage> loginCancelExceptionHandle(BusinessException e) {
        log.warn("loginCancelException : {}", e);
        return new BaseResponseDto(e.getErrorMessage());
    }
}
