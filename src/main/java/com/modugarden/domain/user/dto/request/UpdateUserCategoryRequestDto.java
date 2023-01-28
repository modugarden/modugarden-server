package com.modugarden.domain.user.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class UpdateUserCategoryRequestDto {
    private List<String> categories;
}
