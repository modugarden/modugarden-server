package com.modugarden.common.error.exception.custom;

import com.modugarden.common.error.enums.ErrorMessage;

public class InvalidTokenException extends BusinessException{
    public InvalidTokenException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
