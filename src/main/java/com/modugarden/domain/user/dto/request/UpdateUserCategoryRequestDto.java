package com.modugarden.domain.user.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class UpdateUserCategoryRequestDto {
    @NotNull
    private List<String> categories;
}
