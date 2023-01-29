package com.modugarden.domain.user.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class NicknameIsDuplicatedRequestDto {
    @Pattern(regexp = "^[a-zA-Z0-9_]{2,25}$",
            message = "닉네임는 영어, 숫자, _(언더바)만 사용가능합니다.")
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min=2, max=25, message = "2자 ~ 25자의 닉네임이어야 합니다.")
    private String nickname;
}
