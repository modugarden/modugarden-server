package com.modugarden.domain.curation.dto.response;

import com.modugarden.domain.curation.entity.Curation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurationFollowFeedResponseDto {
    private Long curation_id;
    private String title;
    private LocalDateTime created_Date;
    private String image;
    private String link;
    private Long user_id;
    private String user_nickname;
    private String user_profile_image;
    private String category_category;
    private boolean isLiked;
    private boolean isSaved;

    public CurationFollowFeedResponseDto(Curation curation, boolean like, boolean save) {
        this.curation_id = curation.getId();
        this.title=curation.getTitle();
        this.created_Date=curation.getCreatedDate();
        this.image = curation.getPreviewImage();
        this.link = curation.getLink();
        this.user_id=curation.getUser().getId();
        this.user_nickname=curation.getUser().getNickname();
        this.user_profile_image=curation.getUser().getProfileImg();
        this.category_category=curation.getCategory().getCategory();
        this.isLiked=like;
        this.isSaved=save;
    }
}
