package com.f2z.gach.Entity.EnumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Properties {
    OK(200, "정상 접속"),
    CREATED(201, "DB에 데이터가 생성됨"),
    NO_CONTENT(204, "정상 접속이나, 줄 데이터가 없음."),
    BAD_REQUEST(400, "비정상적인 요청"),
    UNAUTHORIZED(401, "권한 없음"),
    FORBIDDEN(403, "금지된 접근"),
    NOT_FOUND(404, "찾을 수 없음"),
    CONFLICT(409, "충돌 발생"),
    GONE(410, "삭제된 리소스"),
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류"),
    SERVICE_UNAVAILABLE(503, "서비스 이용 불가능"),;

    private final int code;
    private final String message;

}
