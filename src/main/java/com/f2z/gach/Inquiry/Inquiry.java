package com.f2z.gach.Inquiry;

import com.f2z.gach.Config.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Inquiry extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inquiryId;
    private boolean inquiryProgress;
    private Long userCode;
    private String inquiryTitle;
    private String inquiryContent;
    private String inquiryAnswer;

    // 문의 종류 반영 해야 함.

}
