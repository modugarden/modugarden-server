package com.modugarden.domain.user.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class UserNicknameRequestDto {
    @NotEmpty(message = "닉네임을 입력해 주세요.")
    @Size(min= 2, max = 25, message = "길이는 2~25자여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9_]{2,25}$", message = "숫자/영어/_(언더바)만 사용이 가능합니다.")
    private String nickname;
}
