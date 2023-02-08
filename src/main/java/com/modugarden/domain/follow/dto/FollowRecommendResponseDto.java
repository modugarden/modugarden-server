package com.modugarden.domain.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class FollowRecommendResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;
    private List<String> categories;
    private Boolean isFollow;

    public FollowRecommendResponseDto(Long userId, String nickname, String profileImage, List<String> categories) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.categories = categories;
        this.isFollow = false; // 쿼리상 내가 팔로잉하고 있는 유저는 추천에서 제외
    }
}
