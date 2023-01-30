package com.modugarden.domain.fcm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddFcmTokenResponseDto {
    private String fcmToken;
    private Long userId; // 유저 관련해서 무슨 정보가 필요한가?? 여쭤볼것
}
