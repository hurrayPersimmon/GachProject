package com.f2z.gach.Inquiry.DTO;

import com.f2z.gach.EnumType.InquiryCategory;
import com.f2z.gach.Inquiry.Entity.Inquiry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class InquiryResponseDTO {

    private Integer inquiryId;
    private boolean inquiryProgress;
    private InquiryCategory inquiryCategory;
    private Long userId;
    private String inquiryTitle;
    private String inquiryContent;
    private String inquiryAnswer;
    private LocalDateTime createDt;

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

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class saveInquirySuccess {
        private Integer inquiryId;
        private Long userId;

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class InquiryListStructure {
        private Integer inquiryId;
        private InquiryCategory inquiryCategory;
        private LocalDateTime createDt;
        private Boolean inquiryProgress;
        private Long userId;
        private String inquiryTitle;

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


    public static InquiryListStructure toInquiryListResponseDTO(Inquiry inquiry) {
        return InquiryListStructure.builder()
                .inquiryId(inquiry.getInquiryId())
                .inquiryCategory(inquiry.getInquiryCategory())
                .createDt(inquiry.getCreateDt())
                .inquiryProgress(inquiry.isInquiryProgress())
                .userId(inquiry.getUserId())
                .inquiryTitle(inquiry.getInquiryTitle())
                .build();
    }

    public static List<InquiryListStructure> toInquiryResponseList(List<Inquiry> inquiryList) {
        return inquiryList.stream()
                .map(InquiryResponseDTO::toInquiryListResponseDTO)
                .toList();
    }

    public static InquiryResponseDTO toInquiryResponseDTO(Inquiry inquiry) {
        return InquiryResponseDTO.builder()
                .inquiryId(inquiry.getInquiryId())
                .inquiryProgress(inquiry.isInquiryProgress())
                .userId(inquiry.getUserId())
                .inquiryCategory(inquiry.getInquiryCategory())
                .inquiryTitle(inquiry.getInquiryTitle())
                .inquiryContent(inquiry.getInquiryContent())
                .inquiryAnswer(inquiry.getInquiryAnswer())
                .createDt(inquiry.getCreateDt())
                .build();
    }

    public static saveInquirySuccess toRespondSuccess(Inquiry inquiry) {
        return saveInquirySuccess.builder()
                .inquiryId(inquiry.getInquiryId())
                .userId(inquiry.getUserId())
                .build();

    }
}
