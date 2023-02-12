package com.modugarden.domain.user.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class UpdateProfileRequestDto {

    private String nickname;

    @NotNull
    private List<String> categories;
}
