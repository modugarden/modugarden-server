package com.modugarden.common.error.exception.custom;

import com.modugarden.common.error.enums.ErrorMessage;

public class LoginCancelException extends BusinessException{

    public LoginCancelException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
