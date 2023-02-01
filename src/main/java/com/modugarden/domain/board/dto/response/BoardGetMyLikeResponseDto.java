package com.modugarden.domain.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardGetMyLikeResponseDto {
    private Long user_id;
    private Long board_id;
    private boolean check;
}
