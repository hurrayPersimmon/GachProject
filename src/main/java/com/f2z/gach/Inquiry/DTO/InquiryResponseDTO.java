package com.f2z.gach.Inquiry.DTO;

import com.f2z.gach.EnumType.InquiryCategory;
import com.f2z.gach.Inquiry.Entity.Inquiry;
import com.f2z.gach.User.Entity.User;
import lombok.*;
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

    public static InquiryResponseDTO toInquiryResponseDTO(Inquiry inquiry) {
        return InquiryResponseDTO.builder()
                .inquiryId(inquiry.getInquiryId())
                .inquiryProgress(inquiry.isInquiryProgress())
                .inquiryCategory(inquiry.getInquiryCategory())
                .userId(inquiry.getUser().getUserId())
                .inquiryTitle(inquiry.getInquiryTitle())
                .inquiryContent(inquiry.getInquiryContent())
                .inquiryAnswer(inquiry.getInquiryAnswer())
                .createDt(inquiry.getCreateDt())
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class InquiryListStructureForClient {
        private Integer inquiryId;
        private InquiryCategory inquiryCategory;
        private LocalDateTime createDt;
        private Boolean inquiryProgress;
        private Long userId;
        private String inquiryTitle;


    }

    public static List<InquiryListStructureForClient> toInquiryResponseListForClient(List<Inquiry> inquiryList) {
        return inquiryList.stream()
                .map(InquiryResponseDTO::toInquiryListClientResponseDTO)
                .toList();
    }

    public static InquiryListStructureForClient toInquiryListClientResponseDTO(Inquiry inquiry) {
        return InquiryListStructureForClient.builder()
                .inquiryId(inquiry.getInquiryId())
                .inquiryCategory(inquiry.getInquiryCategory())
                .createDt(inquiry.getCreateDt())
                .inquiryProgress(inquiry.isInquiryProgress())
                .userId(inquiry.getUser().getUserId())
                .inquiryTitle(inquiry.getInquiryTitle())
                .build();
    }


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class saveInquirySuccess {
        private Integer inquiryId;
        private Long userId;

    }

    public static saveInquirySuccess toRespondSuccess(Inquiry inquiry) {
        return saveInquirySuccess.builder()
                .inquiryId(inquiry.getInquiryId())
                .userId(inquiry.getUser().getUserId())
                .build();

    }


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class InquiryListStructureForAdmin {
        private Integer inquiryId;
        private InquiryCategory inquiryCategory;
        private LocalDateTime createDt;
        private Boolean inquiryProgress;
        private User user;
        private String inquiryTitle;

        public static InquiryListStructureForAdmin toInquiryListResponseDTO(Inquiry inquiry) {
            return InquiryListStructureForAdmin.builder()
                    .inquiryId(inquiry.getInquiryId())
                    .inquiryCategory(inquiry.getInquiryCategory())
                    .createDt(inquiry.getCreateDt())
                    .inquiryProgress(inquiry.isInquiryProgress())
                    .user(inquiry.getUser())
                    .inquiryTitle(inquiry.getInquiryTitle())
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class InquiryList {
        List<InquiryListStructureForAdmin> inquiryList;
        Integer totalPage;
        Long totalElements;
    }

    public static InquiryList toInquiryResponseList(Page<Inquiry> inquiryPages, List<InquiryListStructureForAdmin> inquiryList) {
        return InquiryList.builder()
                .inquiryList(inquiryList)
                .totalPage(inquiryPages.getTotalPages())
                .totalElements(inquiryPages.getTotalElements())
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class InquiryAnswerResponseDTO {
        private Integer inquiryId;
        private String inquiryAnswer;


    }
}
