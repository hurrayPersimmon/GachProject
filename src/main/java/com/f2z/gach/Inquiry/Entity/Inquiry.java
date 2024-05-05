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

    /** "@ManyToOne"은 외래키인 "User"의 pk가 자식 엔티티인에 존재함.
     *  "@OneToMany"는 부모 엔티티에 "Inquiry"에 대한 pk가 존재하게 됨.
     *  이 외의 내부적인 설정 요소로 "fetch", "cascade"가 존재. 추후 설정 필요
     */
    // TODO : 컨트롤러 코드를 "eventController"처럼 대략적으로 작성해주면 "fetch"와 "cascade"를 어떻게 설정할 것인지 같이 이야기해보면 될 거 같아
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;
    private boolean inquiryProgress;
    private Long userId;
    private String inquiryTitle;
    private String inquiryContent;
    private String inquiryAnswer;
    private InquiryCategory inquiryCategory;
}
