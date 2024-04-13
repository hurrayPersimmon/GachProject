package com.f2z.gach.Entity.User;

import com.f2z.gach.Entity.EnumType.Departments;
import com.f2z.gach.Entity.EnumType.Gender;
import com.f2z.gach.Entity.EnumType.Speed;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class UserGuest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public UserGuest (Integer guestId, Departments guestDepartment, Speed guestSpeed, Gender guestGender, Date guestBirth, Double guestHeight, Double guestWeight) {
        this.guestId = guestId;
        this.guestDepartment = guestDepartment;
        this.guestSpeed = guestSpeed;
        this.guestGender = guestGender;
        this.guestBirth = guestBirth;
        this.guestHeight = guestHeight;
        this.guestWeight = guestWeight;
    }

}
