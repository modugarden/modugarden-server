package com.modugarden.domain.user.dto;

import com.modugarden.domain.category.entity.UserInterestCategory;
import com.modugarden.domain.user.entity.enums.UserAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserInfoResponseDto {
    private String email;
    private String nickname;
    private String birth;
    private UserAuthority userAuthority;
    private String profileImage;
    private List<UserInterestCategory> categories;
}
