package com.f2z.gach.Inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

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
