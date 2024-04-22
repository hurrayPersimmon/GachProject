package com.f2z.gach.User.DTOs;

import com.f2z.gach.EnumType.Departments;
import com.f2z.gach.EnumType.Gender;
import com.f2z.gach.EnumType.Speed;
import com.f2z.gach.User.Entities.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


public class UserResponseDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class respondUserId {
        private Long userId;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class respondUsername {
        private String username;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class provideUserDetailInfo {
        private Long userId;
//        @Enumerated(EnumType.STRING)
//        private Departments userDepartment;
        private String username;
        private String userNickname;
        @Enumerated(EnumType.ORDINAL)
        private Speed userSpeed;
        @Enumerated(EnumType.STRING)
        private Gender userGender;
        private Integer userBirth;
        private Double userHeight;
        private Double userWeight;
    }

    public static provideUserDetailInfo toProvideUserDetailInfo(User user) {
        return provideUserDetailInfo.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .userNickname(user.getUserNickname())
                .userSpeed(user.getUserSpeed())
                .userGender(user.getUserGender())
                .userBirth(user.getUserBirth())
                .userHeight(user.getUserHeight())
                .userWeight(user.getUserWeight())
                .build();
    }

}
