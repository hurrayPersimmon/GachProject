package com.f2z.gach.DTO;

import com.f2z.gach.Entity.EnumType.Authorization;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class AdminDTO {
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
