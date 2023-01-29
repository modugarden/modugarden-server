package com.modugarden.domain.curation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurationGetMyStorageResponseDto {
    private Long user_id;
    private Long curation_id;
    private boolean check;
}
