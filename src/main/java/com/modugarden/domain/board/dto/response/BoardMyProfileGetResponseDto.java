package com.modugarden.domain.board.dto.response;

import com.modugarden.domain.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardMyProfileGetResponseDto {
    private Long id;
    private String image;

    public BoardMyProfileGetResponseDto(Board board) {
        this.id=board.getId();
        this.image = board.getPreview_img();
    }
}
