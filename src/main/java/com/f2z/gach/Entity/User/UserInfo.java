package com.f2z.gach.Entity.User;

import com.f2z.gach.Entity.EnumType.Departments;
import com.f2z.gach.Entity.EnumType.Gender;
import com.f2z.gach.Entity.EnumType.Speed;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class UserInfo {
    @Id
    @ManyToOne(targetEntity = User.class)
    private Long userCode;

    @Enumerated(EnumType.STRING)
    private Departments UserDepartment;
    private String userNickname;
    @Enumerated(EnumType.ORDINAL)
    private Speed userSpeed;
    @Enumerated(EnumType.STRING)
    private Gender userGender;
    private Date userBirth;
    private Double userHeight;
    private Double userWeight;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "userCode", referencedColumnName = "userId")
    private User user;


    @Builder
    public UserInfo(Long userCode, Departments userDepartment, String userNickname, Speed userSpeed, Gender userGender, Date userBirth, Double userHeight, Double userWeight) {
    }
}
