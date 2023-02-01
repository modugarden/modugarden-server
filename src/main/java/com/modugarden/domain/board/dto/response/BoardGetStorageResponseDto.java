package com.modugarden.domain.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardGetStorageResponseDto {
    private Long board_id;
    private String title;
    private Long like_num;
    private String preview_img;
    private Long user_id;
    private String user_nickname;
    private String user_profile_image;
    private String category_category;
}
