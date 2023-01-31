package com.modugarden.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserNotificationResponseDto {

    private Long id;
    private Boolean commentOnOff;
    private Boolean followOnOff;
    private Boolean serviceOnOff;
    private Boolean marketingOnOff;
}
