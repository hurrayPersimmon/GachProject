package com.f2z.gach.User.Entities;

import com.f2z.gach.EnumType.Departments;
import com.f2z.gach.EnumType.Gender;
import com.f2z.gach.EnumType.Speed;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "`USER`") // 백틱으로 감싼 user 테이블 이름
public class User {
    @Id
    @Tsid
    private Long userId;
    private String username;
    private String password;

//    @Enumerated(EnumType.STRING)
//    private Departments userDepartment;
    private String userNickname;
    @Enumerated(EnumType.ORDINAL)
    private Speed userSpeed;
    @Enumerated(EnumType.STRING)
    private Gender userGender;
    @DateTimeFormat(pattern = "yyyy")
    private LocalDate userBirth;
    private Double userHeight;
    private Double userWeight;

    public void updateUserInfo(User user){
        this.username = user.getUsername();
        this.password = user.getPassword();
//        this.userDepartment = user.getUserDepartment();
        this.userNickname = user.getUserNickname();
        this.userSpeed = user.getUserSpeed();
        this.userGender = user.getUserGender();
        this.userBirth = user.getUserBirth();
        this.userHeight = user.getUserHeight();
        this.userWeight = user.getUserWeight();
    }
}
