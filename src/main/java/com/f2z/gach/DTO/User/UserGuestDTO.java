package com.f2z.gach.DTO.User;

import com.f2z.gach.Entity.EnumType.Departments;
import com.f2z.gach.Entity.EnumType.Gender;
import com.f2z.gach.Entity.EnumType.Speed;
import com.f2z.gach.Entity.User.UserGuest;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserGuestDTO {
    private Integer guestId;
    @Enumerated(EnumType.STRING)
    private Departments guestDepartment;
    @Enumerated(EnumType.ORDINAL)
    private Speed guestSpeed;
    @Enumerated(EnumType.STRING)
    private Gender guestGender;
    private Date guestBirth;
    private Double guestHeight;
    private Double guestWeight;

    public UserGuest toEntity() {
        return new UserGuest(guestId, guestDepartment, guestSpeed, guestGender, guestBirth, guestHeight, guestWeight);
    }

}
