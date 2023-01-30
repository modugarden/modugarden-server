package com.modugarden.domain.fcm.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class DeleteFcmTokenRequestDto {
    @NotBlank(message = "삭제할 fcm 토큰은 필수 입력 값입니다.")
    private String fcmToken;
}
