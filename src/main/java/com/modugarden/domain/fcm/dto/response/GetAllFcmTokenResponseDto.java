package com.modugarden.domain.fcm.dto.response;

import com.modugarden.domain.fcm.entity.FcmToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetAllFcmTokenResponseDto {
    List<String> fcmTokens;
}
