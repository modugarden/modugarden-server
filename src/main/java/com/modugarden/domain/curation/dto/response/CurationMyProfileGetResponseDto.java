package com.modugarden.domain.curation.dto.response;

import com.modugarden.domain.curation.entity.Curation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurationMyProfileGetResponseDto {
    private Long id;
    private String image;
    private LocalDateTime created_date;
    private String title;
    private String category;


    public CurationMyProfileGetResponseDto(Curation curation) {
        this.id=curation.getId();
        this.image = curation.getPreviewImage();
        this.created_date=curation.getCreatedDate();
        this.title=curation.getTitle();
        this.category=curation.getCategory().getCategory();
    }
}