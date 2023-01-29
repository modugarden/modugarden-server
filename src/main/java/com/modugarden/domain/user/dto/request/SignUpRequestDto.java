package com.modugarden.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;

@AllArgsConstructor // 임시 코드, 추후 삭제 가능성 있음. CustomOAuth2UserService에서 유저 저장시 DTO생성시 사용중
@Getter
@NoArgsConstructor
public class SignUpRequestDto {

    @Email(message="이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;


    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @Pattern(regexp = "^[a-zA-Z0-9_]{2,25}$",
            message = "닉네임는 영어, 숫자, _(언더바)만 사용가능합니다.")
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min=2, max=25, message = "2자 ~ 25자의 닉네임이어야 합니다.")
    private String nickname;

    @Pattern(regexp="(19[0-9][0-9]|20\\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])",
            message = "올바르지 않은 생년월일 형식입니다. 올바른 형식 ex) 20001231")
    @NotBlank(message = "생년월일은 필수 입력 값입니다.")
    private String birth;

    @NotNull(message="카테고리는 필수 입력 값입니다.")
    private List<String> categories;

    @NotNull(message="소셜로그인으로 인한 회원가입인지 여부는 필수 입력 값입니다.")
    private Boolean isSocialLogin;
}
