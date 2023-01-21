package com.modugarden.exception.custom;

import com.modugarden.common.status.enums.BaseResponseStatus;

public class LoginCancelException extends BusinessException{

    public LoginCancelException(BaseResponseStatus status) {
        super(status);
    }
}
