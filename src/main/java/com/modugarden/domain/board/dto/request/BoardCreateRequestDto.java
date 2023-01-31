package com.modugarden.domain.board.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Data
@NoArgsConstructor
public class BoardCreateRequestDto {

    @NotEmpty(message = "제목을 입력해주세요")
    @Size(min= 1, max = 25, message = "길이는 1~25자여야 합니다.")
    private String title; // 제목

    private List<String> content;

    private List<String> location; // 위치 태그

    private String category; // 카테고리
}
