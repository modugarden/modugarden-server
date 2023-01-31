package com.modugarden.domain.fcm.controller;


import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.fcm.dto.request.AddFcmTokenRequestDto;
import com.modugarden.domain.fcm.dto.request.DeleteFcmTokenRequestDto;
import com.modugarden.domain.fcm.dto.request.UpdateFcmTokenRequestDto;
import com.modugarden.domain.fcm.dto.response.AddFcmTokenResponseDto;
import com.modugarden.domain.fcm.dto.response.DeleteFcmTokenResponseDto;
import com.modugarden.domain.fcm.dto.response.GetAllFcmTokenResponseDto;
import com.modugarden.domain.fcm.dto.response.UpdateFcmTokenResponseDto;
import com.modugarden.domain.fcm.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/fcm")
public class FcmController {

    private final FcmService fcmService;

    @PostMapping()
    public BaseResponseDto<AddFcmTokenResponseDto> addFcmToken(@AuthenticationPrincipal ModugardenUser modugardenUser, @RequestBody @Valid AddFcmTokenRequestDto requestDto){
        AddFcmTokenResponseDto responseDto = fcmService.addFcmToken(requestDto, modugardenUser.getUser());
        return new BaseResponseDto<>(responseDto);
    }

    @PatchMapping
    public BaseResponseDto<UpdateFcmTokenResponseDto> updateFcmToken(@AuthenticationPrincipal ModugardenUser modugardenUser, @RequestBody @Valid UpdateFcmTokenRequestDto requestDto){
        UpdateFcmTokenResponseDto responseDto = fcmService.updateFcmToken(requestDto);
        return new BaseResponseDto(responseDto);
    }

    @GetMapping
    public BaseResponseDto<GetAllFcmTokenResponseDto> getFcmTokens(@AuthenticationPrincipal ModugardenUser modugardenUser){
        GetAllFcmTokenResponseDto fcmTokens = fcmService.getFcmTokens(modugardenUser.getUser());
        return new BaseResponseDto<>(fcmTokens);
    }

    @DeleteMapping
    public BaseResponseDto<DeleteFcmTokenResponseDto> deleteFcmToken(@AuthenticationPrincipal ModugardenUser modugardenUser, @RequestBody @Valid DeleteFcmTokenRequestDto requestDto){
        DeleteFcmTokenResponseDto responseDto = fcmService.deleteFcmToken(requestDto);
        return new BaseResponseDto(responseDto);
    }
}
