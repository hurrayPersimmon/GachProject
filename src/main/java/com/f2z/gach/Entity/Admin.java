package com.f2z.gach.Entity;

import com.f2z.gach.Entity.EnumType.Authorization;
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
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminNum;

    private String adminId;
    private String adminPassword;
    private Date adminBirthday;
    private String adminName;
    @Enumerated(EnumType.STRING)
    private Authorization adminAuthorization;



}
