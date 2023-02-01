package com.modugarden.domain.board.dto.response;

import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.board.entity.BoardImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardGetResponseDto {
    private Long id;
    private String title;
    private Long like_num;
    private LocalDateTime created_Date;
    private List<BoardImage> image;
    private Long user_id;
    private String user_nickname;
    private String user_profile_image;
    private String category_category;


    public BoardGetResponseDto(Board board, List<BoardImage> image) {
        this.id=board.getId();
        this.title=board.getTitle();
        this.created_Date=board.getCreatedDate();
        this.image = image;
        this.like_num = board.getLike_num();
        this.user_id=board.getUser().getId();
        this.user_nickname=board.getUser().getNickname();
        this.user_profile_image=board.getUser().getProfileImg();
        this.category_category=board.getCategory().getCategory();
    }
}

