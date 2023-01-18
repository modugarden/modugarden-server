package com.modugarden.common.status.enums;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    SUCCESS(true,  "요청에 성공하였습니다.");

    private final boolean isSuccess;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }
}