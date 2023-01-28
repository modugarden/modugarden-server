package com.modugarden.domain.user.dto.response;

import com.modugarden.domain.category.entity.UserInterestCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UpdateUserCategoryResponseDto {
    private Long id;
    private List<String> categories;
}
