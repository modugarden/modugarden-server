package com.modugarden.common.error.exception;

import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.common.error.exception.custom.InvalidTokenException;
import com.modugarden.common.error.exception.custom.LoginCancelException;
import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.error.enums.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public BaseResponseDto<ErrorMessage> loginCancelExceptionHandle(LoginCancelException e) {
        log.warn("loginCancelException : {}", e);
        return new BaseResponseDto(e.getErrorMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponseDto<ErrorMessage> MethodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        String firstErrorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();

        // 로그에 에러들 출력
        List<String> errorList = bindingResult.getFieldErrors().stream().map(err -> err.getDefaultMessage()).collect(Collectors.toList());
        System.out.println("errorList = " + errorList);
        String errorMsg = String.join(" | ", errorList);
        System.out.println("errorMsg = " + errorMsg);

        log.warn("MethodArgumen:tNotValidExceptionException : {}", errorMsg);

        return new BaseResponseDto(HttpStatus.BAD_REQUEST.value(), false, firstErrorMessage);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public BaseResponseDto<ErrorMessage> InvalidTokenExceptionHandle(InvalidTokenException e) {
        log.warn("InvalidTokenException : {}", e);
        return new BaseResponseDto(e.getErrorMessage());
    }
}
