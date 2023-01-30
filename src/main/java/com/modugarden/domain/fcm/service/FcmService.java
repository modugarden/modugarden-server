package com.modugarden.domain.fcm.service;

import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.domain.fcm.dto.request.AddFcmTokenRequestDto;
import com.modugarden.domain.fcm.dto.request.DeleteFcmTokenRequestDto;
import com.modugarden.domain.fcm.dto.request.UpdateFcmTokenRequestDto;
import com.modugarden.domain.fcm.dto.response.AddFcmTokenResponseDto;
import com.modugarden.domain.fcm.dto.response.DeleteFcmTokenResponseDto;
import com.modugarden.domain.fcm.dto.response.GetAllFcmTokenResponseDto;
import com.modugarden.domain.fcm.dto.response.UpdateFcmTokenResponseDto;
import com.modugarden.domain.fcm.entity.FcmToken;
import com.modugarden.domain.fcm.repository.FcmRepository;
import com.modugarden.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.modugarden.common.error.enums.ErrorMessage.FCM_TOKEN_NOT_FOUND;

@RequiredArgsConstructor
@Transactional
@Service
public class FcmService {

    private final FcmRepository fcmRepository;

    public AddFcmTokenResponseDto addFcmToken(AddFcmTokenRequestDto requestDto, User user){
        FcmToken newFcmToken = new FcmToken(requestDto.getFcmToken(), user);
        fcmRepository.save(newFcmToken);

        return new AddFcmTokenResponseDto(newFcmToken.getFcmToken(), user.getId());
    }

    public UpdateFcmTokenResponseDto updateFcmToken(UpdateFcmTokenRequestDto requestDto){
        FcmToken fcmToken = fcmRepository.findByFcmToken(requestDto.getBeforeFcmToken()).orElseThrow(() -> new BusinessException(FCM_TOKEN_NOT_FOUND));
        fcmToken.update(requestDto.getUpdateFcmToken());

        return new UpdateFcmTokenResponseDto(requestDto.getUpdateFcmToken());
    }

    public DeleteFcmTokenResponseDto deleteFcmToken(DeleteFcmTokenRequestDto requestDto){
        FcmToken fcmToken = fcmRepository.findByFcmToken(requestDto.getFcmToken()).orElseThrow(() -> new BusinessException(FCM_TOKEN_NOT_FOUND));
        fcmRepository.delete(fcmToken);

        return new DeleteFcmTokenResponseDto(fcmToken.getFcmToken());
    }

    public GetAllFcmTokenResponseDto getFcmTokens(User user){
        List<FcmToken> fcmTokens = fcmRepository.findByUser(user);
        List<String> result = fcmTokens.stream().map(fcm -> fcm.getFcmToken()).collect(Collectors.toList());

        return new GetAllFcmTokenResponseDto(result);
    }
}
