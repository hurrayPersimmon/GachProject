package com.f2z.gach.DTO.User;

import com.f2z.gach.Entity.EnumType.Departments;
import com.f2z.gach.Entity.EnumType.Gender;
import com.f2z.gach.Entity.EnumType.Speed;
import com.f2z.gach.Entity.User.UserInfo;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserInfoDTO {
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


    public UserInfo toEntity() {
        return new UserInfo(userCode, UserDepartment, userNickname, userSpeed, userGender, userBirth, userHeight, userWeight);
    }
}
