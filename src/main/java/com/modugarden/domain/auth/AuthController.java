package com.modugarden.domain.auth;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.auth.dto.request.EmailAuthRequestDto;
import com.modugarden.domain.auth.dto.request.IsEmailDuplicatedRequestDto;
import com.modugarden.domain.auth.dto.request.TokenReissueRequestDto;
import com.modugarden.domain.auth.dto.response.EmailAuthResponseDto;
import com.modugarden.domain.auth.dto.response.IsEmailDuplicatedResponseDto;
import com.modugarden.domain.auth.dto.response.LoginResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.user.dto.request.LoginRequestDto;
import com.modugarden.domain.user.dto.request.NicknameIsDuplicatedRequestDto;
import com.modugarden.domain.user.dto.request.SignUpRequestDto;
import com.modugarden.domain.user.dto.request.SocialLoginRequestDto;
import com.modugarden.domain.user.dto.response.DeleteUserResponseDto;
import com.modugarden.domain.user.dto.response.NicknameIsDuplicatedResponseDto;
import com.modugarden.domain.user.dto.response.SignUpResponseDto;
import com.modugarden.domain.user.service.UserService2;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class AuthController {
    private final UserService2 userService2;
    private final EmailService emailService;


    @ApiOperation(value = "서비스 시작 전 - 회원가입", notes = "회원 정보를 가지고 회원가입을 한다.")
    @PostMapping("/sign-up")
    public BaseResponseDto<SignUpResponseDto> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto){
        Long userId = userService2.signupUser(signUpRequestDto);
        return new BaseResponseDto(new SignUpResponseDto(userId));
    }

    @ApiOperation(value = "서비스 시작 전 - 일반로그인", notes = "이메일, 비밀번호를 사용해 일반 로그인을 한다.")
    @PostMapping("/log-in")
    public BaseResponseDto<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        LoginResponseDto tokenDto = userService2.generalLogin(loginRequestDto);
        return new BaseResponseDto<>(tokenDto);
    }

    @ApiOperation(value = "서비스 시작 전 - 구글 소셜 로그인", notes = "구글 로그인에 성공한 유저의 email을 받아 로그인 한다.")
    @PostMapping("/log-in/social")
    public BaseResponseDto<LoginResponseDto> googleLogin(@RequestBody SocialLoginRequestDto requestDto){
        LoginResponseDto tokenDto = userService2.socialLogin(requestDto);
        return new BaseResponseDto<>(tokenDto);
    }

    @ApiOperation(value = "서비스 시작 전 - 이메일 인증", notes = "입력한 이메일로 인증번호를 전송한다.")
    @PostMapping("/sign-up/email/authentication")
    public BaseResponseDto<EmailAuthResponseDto> mailConfirm(@RequestBody EmailAuthRequestDto requestDto){
        return new BaseResponseDto<>(emailService.sendEmail(requestDto));
    }

    @ApiOperation(value = "서비스 시작 전 - 닉네임 중복 여부 조회", notes = "해당 닉네임을 가진 유저가 있는지 확인한다.")
    @PostMapping("/nickname/isDuplicated")
    public BaseResponseDto<NicknameIsDuplicatedResponseDto> isNicknameDuplicated(@RequestBody NicknameIsDuplicatedRequestDto requestDto){
        return new BaseResponseDto<>(userService2.isNicknameDuplicate(requestDto));
    }

    @ApiOperation(value = "서비스 시작 전 - 일반 회원가입 -  이메일 중복 여부 확인", notes = "이미 해당 이메일로 가입된 회원이 있는지 확인한다.")
    @PostMapping("/sign-up/email/isDuplicated")
    public BaseResponseDto<IsEmailDuplicatedResponseDto> checkEmailDuplicated(@RequestBody IsEmailDuplicatedRequestDto requestDto){
        return new BaseResponseDto<>(userService2.isEmailDuplicate(requestDto));
    }

    @ApiOperation(value = "내 프로필 - 설정 - 회원 탈퇴", notes = "현재 로그인한 유저 회원 탈퇴를 진행한다.")
    @DeleteMapping("/me")
    public BaseResponseDto<DeleteUserResponseDto> deleteCurrentUser(@AuthenticationPrincipal ModugardenUser modugardenUser){
        return new BaseResponseDto<>(userService2.deleteCurrentUser(modugardenUser.getUser()));
    }

    @ApiOperation(value = "accessToken, refreshToken 재발급", notes = "만료된 accessToken과 만료되지 않은 refreshToken을 사용해 새로운 accessToken과 refreshToken을 발급받는다.")
    @PostMapping("/token-reissue")
    public BaseResponseDto<LoginResponseDto> reissueToken(@RequestBody @Valid TokenReissueRequestDto requestDto){
        LoginResponseDto tokenDto = userService2.reissueAccessToken(requestDto);
        return new BaseResponseDto<>(tokenDto);
    }

}
