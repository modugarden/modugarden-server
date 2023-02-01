package com.modugarden.domain.curation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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

}
