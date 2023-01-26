package com.modugarden.domain.user.dto.response;

import com.modugarden.domain.user.entity.enums.UserAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserSettingInfoResponseDto {

    private Long id;
    private String email;
    private String nickname;
    private String birth;
    private UserAuthority userAuthority;
    private String profileImage;
    private List<String> categories;
}
