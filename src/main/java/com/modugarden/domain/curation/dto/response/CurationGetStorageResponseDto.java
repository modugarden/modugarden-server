package com.modugarden.domain.curation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.curation.entity.Curation;
import com.modugarden.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurationGetStorageResponseDto {
    @JsonProperty
    private Long id;
    private String title;
    private String link;
    private String preview_image;
    private Long likeNum;
    private Long user_id;
    private String user_nickname;
    private String user_profile_image;
    private String category_category;


    public CurationGetStorageResponseDto(Curation curation) {
        this.id = curation.getId();
        this.title = curation.getTitle();
        this.link = curation.getLink();
        this.preview_image = curation.getPreviewImage();
        this.likeNum = curation.getLikeNum();
        this.user_id = curation.getUser().getId();
        this.user_nickname = curation.getUser().getNickname();
        this.user_profile_image = curation.getUser().getProfileImg();
        this.category_category = curation.getCategory().getCategory();
    }
}
