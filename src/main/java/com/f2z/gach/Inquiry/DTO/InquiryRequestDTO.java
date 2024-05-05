package com.f2z.gach.Inquiry.DTO;

import com.f2z.gach.Inquiry.Entity.Inquiry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class InquiryRequestDTO {

    private Long userId;
    private String inquiryTitle;
    private String inquiryContent;

    public static Inquiry toEntity(InquiryRequestDTO inquiryRequestDTO) {
        return Inquiry.builder()
                .userId(inquiryRequestDTO.getUserId())
                .inquiryTitle(inquiryRequestDTO.getInquiryTitle())
                .inquiryContent(inquiryRequestDTO.getInquiryContent())
                .build();
    }
}
