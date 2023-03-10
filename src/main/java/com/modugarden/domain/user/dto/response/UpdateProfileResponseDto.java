package com.modugarden.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UpdateProfileResponseDto {
    private Long id;
    private String nickname;
    private List<String> categories;
}
