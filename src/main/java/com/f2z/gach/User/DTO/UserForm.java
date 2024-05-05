package com.f2z.gach.User.DTO;

import com.f2z.gach.EnumType.Gender;
import com.f2z.gach.EnumType.Speed;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserForm {
    private String username;
    private String userNickname;
    private Integer userBirth;
    private Double userHeight;
    private Double userWeight;
    @Enumerated(EnumType.ORDINAL)
    private Speed userSpeed;
    @Enumerated(EnumType.STRING)
    private Gender userGender;
}
