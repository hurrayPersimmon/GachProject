package com.f2z.gach.Entity;

import com.f2z.gach.Entity.EnumType.Departments;
import com.f2z.gach.Entity.EnumType.Gender;
import com.f2z.gach.Entity.EnumType.Speed;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
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

}
