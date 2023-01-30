package com.modugarden.domain.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardLikeResponseDto {
    private Long id;
    private Long like_num;
}
