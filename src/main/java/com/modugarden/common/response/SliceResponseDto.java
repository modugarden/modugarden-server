package com.modugarden.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SliceResponseDto<T> implements Serializable {
    private List<T> content;
    private boolean hasNext;
    private boolean isFirst;
    private boolean isLast;

    public SliceResponseDto(Slice<T> slice){
            this.content = slice.getContent();
            this.hasNext = slice.hasNext();
            this.isFirst = slice.isFirst();
            this.isLast = slice.isLast();
    }
}