package com.modugarden.domain.curation.dto.request;

import com.modugarden.domain.curation.entity.Curation;
import com.modugarden.domain.like.entity.LikeCuration;
import com.modugarden.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurationLikeRequestDto {

    private User user; // 유저
    private Curation curation; // 카테고리

    public LikeCuration toEntity() {
        return LikeCuration.builder()
                .user(user)
                .curation(curation)
                .build();
    }
}