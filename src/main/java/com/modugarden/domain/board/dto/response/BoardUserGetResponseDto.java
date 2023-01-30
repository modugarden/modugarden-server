package com.modugarden.domain.board.dto.response;


import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.board.entity.BoardImage;
import com.modugarden.domain.curation.entity.Curation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardUserGetResponseDto {
    private Long board_id;
    private Long id;
    private String title;
    private Long like_num;
    private LocalDateTime created_Date;
    private String image;
    private Long user_id;
    private String user_nickname;
    private String user_profile_image;
    private String category_category;


    public BoardUserGetResponseDto(BoardImage boardImage) {
        this.board_id = boardImage.getBoard().getId();
        this.id=boardImage.getId();
        this.title=boardImage.getBoard().getTitle();
        this.created_Date=boardImage.getBoard().getCreatedDate();
        this.image = boardImage.getImage();
        this.like_num = boardImage.getBoard().getLike_num();
        this.user_id=boardImage.getBoard().getUser().getId();
        this.user_nickname=boardImage.getBoard().getUser().getNickname();
        this.user_profile_image=boardImage.getBoard().getUser().getProfileImg();
        this.category_category=boardImage.getBoard().getCategory().getCategory();
    }
}
