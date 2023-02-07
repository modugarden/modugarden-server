package com.modugarden.domain.user.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class UpdateProfileRequestDto {

    private String nickname;

    @NotNull
    private List<String> categories;
}
