package com.modugarden.domain.fcm.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class UpdateFcmTokenRequestDto {
    @NotBlank(message = "수정 전 fcm 토큰은 필수 입력 값입니다.")
    private String beforeFcmToken;
    @NotBlank(message = "수정 후 fcm 토큰은 필수 입력 값입니다.")
    private String updateFcmToken;
}
