package com.f2z.gach.Inquiry.DTO;

import com.f2z.gach.EnumType.InquiryCategory;
import com.f2z.gach.Inquiry.Entity.Inquiry;
import com.f2z.gach.User.Entity.User;
import com.f2z.gach.User.Repository.UserRepository;
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
    private InquiryCategory inquiryCategory;



    public static Inquiry toEntity(InquiryRequestDTO inquiryRequestDTO, User user) {
        return Inquiry.builder()
                .user(user)
                .inquiryTitle(inquiryRequestDTO.getInquiryTitle())
                .inquiryContent(inquiryRequestDTO.getInquiryContent())
                .inquiryCategory(inquiryRequestDTO.getInquiryCategory())
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class InquiryAnswerRequestDTO{
        private Integer inquiryId;
        private String inquiryAnswer;

        public static Inquiry toEntity(InquiryAnswerRequestDTO inquiryAnswerRequestDTO){
            return Inquiry.builder()
                    .inquiryId(inquiryAnswerRequestDTO.getInquiryId())
                    .inquiryAnswer(inquiryAnswerRequestDTO.getInquiryAnswer())
                    .build();
        }
    }

}
