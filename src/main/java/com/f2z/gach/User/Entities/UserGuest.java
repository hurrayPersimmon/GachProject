package com.f2z.gach.User.Entities;

import com.f2z.gach.EnumType.Departments;
import com.f2z.gach.EnumType.Gender;
import com.f2z.gach.EnumType.Speed;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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

    public UserGuest (Integer guestId, Departments guestDepartment, Speed guestSpeed, Gender guestGender, LocalDate guestBirth, Double guestHeight, Double guestWeight) {
        this.guestId = guestId;
        this.guestDepartment = guestDepartment;
        this.guestSpeed = guestSpeed;
        this.guestGender = guestGender;
        this.guestBirth = guestBirth;
        this.guestHeight = guestHeight;
        this.guestWeight = guestWeight;
    }

}
