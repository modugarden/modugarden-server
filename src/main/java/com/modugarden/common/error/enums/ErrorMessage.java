package com.modugarden.common.error.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorMessage {

    /**
     * 성공
     */
    SUCCESS(OK, true,  "요청에 성공하였습니다."),

    /**
     * 실패
     */
    INTERVAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, false,"요청을 처리하는 과정에서 서버가 예상하지 못한 오류가 발생하였습니다."),

    // USER
    USER_NOT_FOUND(NOT_FOUND, false, "해당 회원을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(NOT_FOUND, false, "해당 회원의 카테고리를 찾을 수 없습니다."),
    WRONG_NICKNAME(BAD_REQUEST, false, "닉네임을 입력해야 합니다."),
    BLOCKUSER_NOT_FOUND(NOT_FOUND, false, "해당 차단 목록을 찾을 수 없습니다."),
    WRONG_PASSWORD(NOT_FOUND, false, "비밀번호가 맞지 않습니다."),
    ALREADY_SIGNUPED_EMAIL_USER(BAD_REQUEST, false, "이미 회원가입한 유저입니다."),
    UNAUTHORIZED_USER(UNAUTHORIZED, false, "인증되지 않은 유저입니다."),
    FORBIDDEN_USER(FORBIDDEN, false ,"접근 권한이 없습니다."),
    TOKEN_IS_NULL(BAD_REQUEST, false, "토큰이 없습니다." ),
    SEND_EMAIL_ERROR(BAD_REQUEST, false, "이메일 인증 코드 전송을 실패하였습니다."),
    WRONG_REISSUE_TOKEN_ACCESS(BAD_REQUEST, false, "RefreshToken 탈취가 의심됩니다. 재 로그인해주세요."),
    FCM_TOKEN_NOT_FOUND(BAD_REQUEST, false, "해당 FCM 토큰이 없습니다."),
    WRONG_CATEGORY(BAD_REQUEST, false, "카테고리를 입력해야 합니다."),
    ALREADY_REPORT_USER(BAD_REQUEST, false, "이미 신고한 회원입니다."),


    // POST
    WRONG_BOARD(NOT_FOUND,false,"해당 포스트를 찾을 수 없습니다."),
    WRONG_BOARD_LIST(NOT_FOUND,false,"해당 포스트 리스트를 찾을 수 없습니다."),
    WRONG_BOARD_FILE(NOT_FOUND,false,"해당 포스트 파일을 찾을 수 없습니다."),
    WRONG_BOARD_DELETE(NOT_FOUND,false,"해당 포스트를 삭제할 수 없습니다."),
    WRONG_BOARD_STORAGE(NOT_FOUND,false,"이미 포스트가 저장되어 있습니다."),
    ALREADY_REPORT_BOARD(BAD_REQUEST, false, "이미 신고한 포스트입니다."),


    // CURATION
    WRONG_CURATION(NOT_FOUND,false,"해당 큐레이션을 찾을 수 없습니다."),
    WRONG_CURATION_LIST(NOT_FOUND,false,"해당 큐레이션 리스트를 찾을 수 없습니다."),
    WRONG_CURATION_FILE(NOT_FOUND,false,"큐레이션 이미지 파일을 찾을 수 없습니다."),
    WRONG_CURATION_DELETE(NOT_FOUND,false,"해당 큐레이션을 삭제할 수 없습니다."),
    WRONG_CURATION_STORAGE(NOT_FOUND,false,"이미 큐레이션이 저장되어 있습니다."),
    ALREADY_REPORT_CURATION(BAD_REQUEST, false, "이미 신고한 큐레이션입니다."),

    // JWT
    WRONG_JWT_SIGNITURE(NOT_FOUND, false, "잘못된 JWT 서명입니다."),
    EXPIRED_JWT_TOKEN(UNAUTHORIZED, false, "만료된 JWT 토큰입니다."),
    NOT_APPLY_JWT_TOKEN(NOT_FOUND, false, "지원되지 않는 JWT 토큰입니다."),
    WRONG_JWT_TOKEN(NOT_FOUND, false, "JWT 토큰이 잘못되었습니다."),

    // FOLLOW
    FOLLOW_NOT_FOUND(NOT_FOUND, false, "팔로잉을 찾을 수 없습니다."),
    ALREADY_FOLLOWED(BAD_REQUEST, false, "이미 팔로잉되어있는 사람입니다."),

    // COMMENT
    COMMENT_NOT_FOUND(NOT_FOUND, false, "댓글을 찾을 수 없습니다"),
    WRONG_COMMENT(NOT_FOUND,false,"해당 댓글을 찾을 수 없습니다."),
    WRONG_PARENT_COMMENT_ID(BAD_REQUEST, false, "올바르지 않은 부모 댓글 입니다."),
    ALREADY_REPORT_COMMENT(BAD_REQUEST, false, "이미 신고한 댓글입니다."),

    // LIKE
    ALREADY_LIKED_BOARD(BAD_REQUEST, false, "이미 좋아요한 게시물입니다."),

    // 공통
    INVALID_FILE_UPLOAD(BAD_REQUEST, false, "파일 업로드에 실패하였습니다."),
    INVALID_FORMAT(BAD_REQUEST, false, "형식에 맞지 않습니다.");

    private final int code;
    private final boolean isSuccess;
    private final String message;

    ErrorMessage(HttpStatus code, boolean isSuccess, String message) {
        this.code = code.value();
        this.isSuccess = isSuccess;
        this.message = message;
    }
}