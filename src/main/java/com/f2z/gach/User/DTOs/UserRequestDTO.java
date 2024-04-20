package com.f2z.gach.User.DTOs;

import com.f2z.gach.EnumType.Departments;
import com.f2z.gach.EnumType.Gender;
import com.f2z.gach.EnumType.Speed;
import com.f2z.gach.User.Entities.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


public class UserRequestDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserLoginInfo {
        private Long userId;
        private String username;
        private String password;


        public static User toUser(UserLoginInfo userLoginInfo) {
            return User.builder()
                    .userId(userLoginInfo.getUserId())
                    .username(userLoginInfo.getUsername())
                    .password(userLoginInfo.getPassword())
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserDetailInfo {
        private Long userId;
        private String username;
        private String password;
//        @Enumerated(EnumType.STRING)
//        private Departments userDepartment;
        private String userNickname;
        @Enumerated(EnumType.ORDINAL)
        private Speed userSpeed;
        @Enumerated(EnumType.STRING)
        private Gender userGender;
        private Integer userBirth;
        private Double userHeight;
        private Double userWeight;


        public static User toUserDetailInfo(UserDetailInfo userDetailInfo) {
            return User.builder()
                    .username(userDetailInfo.getUsername())
                    .password(userDetailInfo.getPassword())
//                    .userDepartment(userDetailInfo.getUserDepartment())
                    .userNickname(userDetailInfo.getUserNickname())
                    .userSpeed(userDetailInfo.getUserSpeed())
                    .userGender(userDetailInfo.getUserGender())
                    .userBirth(userDetailInfo.getUserBirth())
                    .userHeight(userDetailInfo.getUserHeight())
                    .userWeight(userDetailInfo.getUserWeight())
                    .build();
        }

    }



}
