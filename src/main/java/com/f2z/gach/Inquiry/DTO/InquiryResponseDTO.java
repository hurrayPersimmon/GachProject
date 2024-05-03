package com.f2z.gach.Inquiry.DTO;

import com.f2z.gach.Inquiry.Entity.Inquiry;
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
public class InquiryResponseDTO {

    private Integer inquiryId;
    private boolean inquiryProgress;
    private Long userId;
    private String inquiryTitle;
    private String inquiryContent;
    private String inquiryAnswer;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class InquiryList {
        List<InquiryResponseDTO> inquiryList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean firstPage;
        Boolean lastPage;
    }

    public static InquiryList toInquiryResponseList(Page<Inquiry> inquiryPages, List<InquiryResponseDTO> inquiryList) {
        return InquiryList.builder()
                .inquiryList(inquiryList)
                .listSize(inquiryList.size())
                .totalPage(inquiryPages.getTotalPages())
                .totalElements(inquiryPages.getTotalElements())
                .firstPage(inquiryPages.isFirst())
                .lastPage(inquiryPages.isLast())
                .build();
    }


    public static InquiryResponseDTO toInquiryListResponseDTO(Inquiry inquiry) {
        return InquiryResponseDTO.builder()
                .inquiryId(inquiry.getInquiryId())
                .inquiryProgress(inquiry.isInquiryProgress())
                .userId(inquiry.getUserId())
                .inquiryTitle(inquiry.getInquiryTitle())
                .build();
    }

    public static InquiryResponseDTO toInquiryResponseDTO(Inquiry inquiry) {
        return InquiryResponseDTO.builder()
                .inquiryId(inquiry.getInquiryId())
                .inquiryProgress(inquiry.isInquiryProgress())
                .userId(inquiry.getUserId())
                .inquiryTitle(inquiry.getInquiryTitle())
                .inquiryContent(inquiry.getInquiryContent())
                .inquiryAnswer(inquiry.getInquiryAnswer())
                .build();
    }

    public static InquiryResponseDTO toRespondSuccess(Inquiry inquiry) {
        return InquiryResponseDTO.builder()
                .inquiryId(inquiry.getInquiryId())
                .userId(inquiry.getUserId())
                .build();

    }
}
