package com.f2z.gach.Inquiry.Entity;

import com.f2z.gach.Config.BaseTimeEntity;
import com.f2z.gach.EnumType.InquiryCategory;
import com.f2z.gach.User.Entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Inquiry extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inquiryId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userId", updatable = false)
    private User user;

    private boolean inquiryProgress;
    private String inquiryTitle;
    private String inquiryContent;
    private String inquiryAnswer;
    private InquiryCategory inquiryCategory;

    public void update(String inquiryAnswer) {
        this.inquiryProgress = true;
        this.inquiryAnswer = inquiryAnswer;
    }
}
