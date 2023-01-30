package com.modugarden.domain.board.dto.request;

import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.board.entity.BoardImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardCreateImageReqeuestDto {

    private String image; // 이미지

    private String content; // 내용

    private Board board; // 해당하는 포스트

    public BoardImage toEntity() {
        return BoardImage.builder()
                .image(image)
                .content(content)
                .board(board)
                .build();
    }
}
