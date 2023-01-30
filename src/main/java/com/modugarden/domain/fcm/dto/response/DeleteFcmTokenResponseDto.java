package com.modugarden.domain.fcm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteFcmTokenResponseDto {
    private String fcmToken;
}
