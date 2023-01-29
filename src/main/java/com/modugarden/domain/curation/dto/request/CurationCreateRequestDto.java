package com.modugarden.domain.curation.dto.request;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Data
@NoArgsConstructor
public class CurationCreateRequestDto {

    @NotEmpty(message = "제목을 입력해주세요")
    @Size(min= 1, max = 25, message = "길이는 1~25자여야 합니다.")
    private String title; // 제목

    @NotEmpty(message = "링크를 입력해주세요")
    private String link; // 링크

    private String category; // 카테고리
}

