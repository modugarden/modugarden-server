package com.modugarden.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageResponseDto<T> implements Serializable{
    List<T> content;
    long totalElements;
    int totalPages;
    int pageNumber;
    int numberOfElements;
    boolean hasNextPages;

    public PageResponseDto(Page<T> page) {
        this.content = page.getContent();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.pageNumber = page.getNumber();
        this.numberOfElements = page.getNumberOfElements();
        this.hasNextPages = page.hasNext();
    }
}