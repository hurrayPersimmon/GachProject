package com.f2z.gach.Inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class InquiryResponseDTO {

    private Integer inquiryId;
    private boolean inquiryProgress;
    private Long userCode;
    private String inquiryTitle;
    private String inquiryContent;
    private String inquiryAnswer;
}
