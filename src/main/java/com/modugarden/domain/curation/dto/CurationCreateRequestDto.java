package com.modugarden.domain.curation.dto;

import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.curation.entity.Curation;
import com.modugarden.domain.user.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
public class CurationCreateRequestDto {

    private String title; // 제목
    private String link; // 링크
    private InterestCategory category; //카테고리
}

