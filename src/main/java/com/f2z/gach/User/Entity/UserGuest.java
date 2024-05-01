package com.f2z.gach.User.Entity;

import com.f2z.gach.EnumType.Gender;
import com.f2z.gach.EnumType.Speed;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Builder
public class UserGuest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer guestId;

//    @Enumerated(EnumType.STRING)
//    private Departments guestDepartment;
    @Enumerated(EnumType.ORDINAL)
    private Speed guestSpeed;
    @Enumerated(EnumType.STRING)
    private Gender guestGender;
    private Integer guestBirth;
    private Double guestHeight;
    private Double guestWeight;

    public UserGuest (Integer guestId, Speed guestSpeed, Gender guestGender, Integer guestBirth, Double guestHeight, Double guestWeight) {
        this.guestId = guestId;
//        this.guestDepartment = guestDepartment;
        this.guestSpeed = guestSpeed;
        this.guestGender = guestGender;
        this.guestBirth = guestBirth;
        this.guestHeight = guestHeight;
        this.guestWeight = guestWeight;
    }

}
