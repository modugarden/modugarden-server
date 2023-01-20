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
    private String previewImage; // 미리보기
    private User user; // 유저
    private InterestCategory category; //카테고리

    public Curation toEntity() {
        Curation curation = Curation.builder()
                .title(title)
                .link(link)
                .previewImage(previewImage)
                .user(user)
                .category(category)
                .build();

        return curation;
    }
}

