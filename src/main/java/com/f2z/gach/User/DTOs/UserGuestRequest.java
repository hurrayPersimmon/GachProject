package com.f2z.gach.User.DTOs;

import com.f2z.gach.EnumType.Departments;
import com.f2z.gach.EnumType.Gender;
import com.f2z.gach.EnumType.Speed;
import com.f2z.gach.User.Entities.UserGuest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserGuestRequest {
    private Integer guestId;
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

    public UserGuest toEntity() {
        return new UserGuest(guestId, guestDepartment, guestSpeed, guestGender, guestBirth, guestHeight, guestWeight);
    }

}
