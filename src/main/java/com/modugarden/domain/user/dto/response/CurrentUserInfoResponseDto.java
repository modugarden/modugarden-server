package com.modugarden.domain.user.dto.response;

import com.modugarden.domain.user.entity.enums.UserAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CurrentUserInfoResponseDto {
    private Long id;
    private String nickname;
    private UserAuthority userAuthority;
    private String profileImage;
    private int followerCount;
    private int postCount;
    private List<String> categories;
}
