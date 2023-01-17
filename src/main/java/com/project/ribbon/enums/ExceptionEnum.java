package com.project.ribbon.enums;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ExceptionEnum {
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "400"), // 클라이언트 or 서버 쿼리에러
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "401"), // 문법에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500"), //서버에러, 요청방식에러

    SECURITY_01(HttpStatus.UNAUTHORIZED, "S0001", "권한이 없습니다."); // 권한에러

    private final HttpStatus status;
    private final String code;
    private String message;

    ExceptionEnum(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }

    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

