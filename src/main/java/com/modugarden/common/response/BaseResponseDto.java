package com.modugarden.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.modugarden.common.status.enums.BaseResponseStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.modugarden.common.status.enums.BaseResponseStatus.SUCCESS;

@Getter
@JsonPropertyOrder({"code", "isSuccess", "message", "result"})
public class BaseResponseDto<T> {
    private final int code;
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 요청에 성공한 경우
    public BaseResponseDto(T result) {
        this.code = HttpStatus.OK.value();
        this.isSuccess = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.result = result;
    }

    // 요청에 실패한 경우
    public BaseResponseDto(BaseResponseStatus status) {
        this.code = status.getCode();
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
    }
}