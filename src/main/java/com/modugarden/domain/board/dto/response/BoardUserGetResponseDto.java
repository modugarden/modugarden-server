package com.modugarden.domain.board.dto.response;


import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.board.entity.BoardImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardUserGetResponseDto {
    private Long id;
    private String image;

    public BoardUserGetResponseDto(Board board) {
        this.id=board.getId();
        this.image = board.getPreview_img();
    }
}
