package com.modugarden.domain.curation.dto.response;

import com.modugarden.domain.curation.entity.Curation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurationGetResponseDto {
    private Long id;
    private String title;
    private String link;
    private String preview_image;
    private Long like_num;
    private LocalDateTime created_Date;
    private Long user_id;
    private String user_nickname;
    private String user_profile_image;
    private String category_category;
    private Boolean isliked;
    private Boolean isSaved;


    public CurationGetResponseDto(Curation curation,Boolean isliked, Boolean isSaved) {
        this.id=curation.getId();
        this.title=curation.getTitle();
        this.link=curation.getLink();
        this.preview_image=curation.getPreviewImage();
        this.like_num =curation.getLikeNum();
        this.created_Date=curation.getCreatedDate();
        this.user_id=curation.getUser().getId();
        this.user_nickname=curation.getUser().getNickname();
        this.user_profile_image=curation.getUser().getProfileImg();
        this.category_category=curation.getCategory().getCategory();
        this.isliked=isliked;
        this.isSaved=isSaved;
    }
}
