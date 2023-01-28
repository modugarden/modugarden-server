package com.modugarden.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UpdateNotificationRequestDto {

    private Boolean commentOnOff;
    private Boolean followOnOff;
    private Boolean serviceOnOff;
    private Boolean marketingOnOff;

}
