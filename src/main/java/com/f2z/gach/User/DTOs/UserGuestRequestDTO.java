package com.f2z.gach.User.DTOs;

import com.f2z.gach.EnumType.Departments;
import com.f2z.gach.EnumType.Gender;
import com.f2z.gach.EnumType.Speed;
import com.f2z.gach.User.Entities.UserGuest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


public class UserGuestRequestDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserGuestRequest {
        private static Integer guestId;
        @Enumerated(EnumType.STRING)
        private Departments guestDepartment;
        @Enumerated(EnumType.ORDINAL)
        private Speed guestSpeed;
        @Enumerated(EnumType.STRING)
        private Gender guestGender;
        @DateTimeFormat(pattern = "yyyy")
        private LocalDate guestBirth;
        private Double guestHeight;
        private Double guestWeight;

        public static UserGuest toEntity(UserGuestRequest userGuestRequest) {
            return UserGuest.builder()
                    .guestDepartment(userGuestRequest.guestDepartment)
                    .guestSpeed(userGuestRequest.guestSpeed)
                    .guestGender(userGuestRequest.guestGender)
                    .guestBirth(userGuestRequest.guestBirth)
                    .guestHeight(userGuestRequest.guestHeight)
                    .guestWeight(userGuestRequest.guestWeight)
                    .build();

        }
    }


}
