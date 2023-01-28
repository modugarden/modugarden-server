package com.modugarden.domain.block.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class BlockUserListResponseDto {

    private Long id;
    private String nickname;
    private String profileImage;
    private List<String> categories;

}
