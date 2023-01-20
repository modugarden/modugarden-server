package com.modugarden.domain.user.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SignUpRequestDto {
    private String email;
    private String password;
    private String nickname;
    private String birth;
    private List<String> categories;
}
