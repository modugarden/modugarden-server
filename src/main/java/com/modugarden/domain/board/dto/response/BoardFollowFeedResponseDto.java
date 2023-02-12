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
public class BoardFollowFeedResponseDto {
    private Long board_id;
    private String title;
    private LocalDateTime created_Date;
    private List<BoardImage> image;
    private Long user_id;
    private String user_nickname;
    private String user_profile_image;
    private String category_category;
    private boolean isLiked;
    private boolean isSaved;
    private List<String> fcmTokens;

    public BoardFollowFeedResponseDto(Board board, List<BoardImage> image, boolean like, boolean save, List<String> fcmTokens) {
        this.board_id = board.getId();
        this.title=board.getTitle();
        this.created_Date=board.getCreatedDate();
        this.image = image;
        this.user_id=board.getUser().getId();
        this.user_nickname=board.getUser().getNickname();
        this.user_profile_image=board.getUser().getProfileImg();
        this.category_category=board.getCategory().getCategory();
        this.isLiked=like;
        this.isSaved=save;
        this.fcmTokens = fcmTokens;
    }
}
