package com.modugarden.domain.board.dto.request;

import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.like.entity.LikeBoard;
import com.modugarden.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardLikeRequestDto {

    private User user; // 유저
    private Board board; // 카테고리

    public LikeBoard toEntity() {
        return LikeBoard.builder()
                .user(user)
                .board(board)
                .build();
    }
}
