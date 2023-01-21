package com.modugarden.exception.custom;

import com.modugarden.common.status.enums.BaseResponseStatus;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final BaseResponseStatus status;

    public BusinessException(BaseResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
