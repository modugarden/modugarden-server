package com.modugarden.domain.board.dto.response;

import com.modugarden.domain.board.entity.Board;
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

    public BoardGetStorageResponseDto(Board board) {
        this.board_id=board.getId();
        this.title=board.getTitle();
        this.like_num = board.getLike_num();
        this.preview_img=board.getPreview_img();
        this.user_id=board.getUser().getId();
        this.user_nickname=board.getUser().getNickname();
        this.user_profile_image=board.getUser().getProfileImg();
        this.category_category=board.getCategory().getCategory();
    }
}
