package com.f2z.gach.EnumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InquiryCategory {
    Node("지점 문의"),
    Route("경로 문의"),
    AITime("AI 시간 문의"),
    Event("이벤트 문의"),
    Place("장소 문의"),
    Etc("기타 문의"),
    AR("AR 문의"),
    Gach("가치 문의");
    private final String title;
}
