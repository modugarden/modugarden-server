package com.modugarden.common.error.exception;

import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.common.error.exception.custom.LoginCancelException;
import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.error.enums.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static com.modugarden.common.error.enums.ErrorMessage.*;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponseDto<ErrorMessage> MethodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<String> errorList = bindingResult.getFieldErrors().stream().map(err -> err.getDefaultMessage()).collect(Collectors.toList());
        System.out.println("errorList = " + errorList);
        String errorMsg = String.join(" | ", errorList);
        System.out.println("errorMsg = " + errorMsg);

        log.warn("MethodArgumentNotValidExceptionException : {}", errorMsg);

        return new BaseResponseDto(INVALID_FORMAT);
    }

}
