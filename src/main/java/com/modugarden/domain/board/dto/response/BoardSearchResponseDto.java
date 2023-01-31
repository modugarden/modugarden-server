package com.modugarden.domain.board.dto.response;


import com.modugarden.domain.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardSearchResponseDto {
    private Long id;
    private String title;
    private String preview_img;
    private Long likeNum;
    private LocalDateTime created_Date;
    private Long user_id;
    private String user_nickname;
    private String user_profile_image;
    private String category_category;


    public BoardSearchResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.preview_img = board.getPreview_img();
        this.likeNum = board.getLike_num();
        this.created_Date = board.getCreatedDate();
        this.user_id = board.getUser().getId();
        this.user_nickname = board.getUser().getNickname();
        this.user_profile_image = board.getUser().getProfileImg();
        this.category_category = board.getCategory().getCategory();
    }
}
