package com.f2z.gach.User.DTO;

import com.f2z.gach.EnumType.Gender;
import com.f2z.gach.EnumType.Speed;
import com.f2z.gach.User.Entity.UserGuest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class UserGuestRequestDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserGuestRequest {
        private static Integer guestId;
//        @Enumerated(EnumType.STRING)
//        private Departments guestDepartment;
        @Enumerated(EnumType.ORDINAL)
        private Speed guestSpeed;
        @Enumerated(EnumType.STRING)
        private Gender guestGender;
        private Integer guestBirth;
        private Double guestHeight;
        private Double guestWeight;

        public static UserGuest toEntity(UserGuestRequest userGuestRequest) {
            return UserGuest.builder()
//                    .guestDepartment(userGuestRequest.guestDepartment)
                    .guestSpeed(userGuestRequest.guestSpeed)
                    .guestGender(userGuestRequest.guestGender)
                    .guestBirth(userGuestRequest.guestBirth)
                    .guestHeight(userGuestRequest.guestHeight)
                    .guestWeight(userGuestRequest.guestWeight)
                    .build();

        }
    }


}
