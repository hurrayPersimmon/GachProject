package com.f2z.gach.DTO;

import com.f2z.gach.Entity.Admin;
import com.f2z.gach.Entity.EnumType.Authorization;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminDTO {
    private Integer adminNum;
    private String adminId;
    private String adminPassword;
    private String adminPasswordCheck;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate adminBirthday;
    private String adminName;
    @Enumerated(EnumType.STRING)
    private Authorization adminAuthorization;

    public Admin toEntity(){
        return new Admin(adminNum, adminId, adminPassword, adminBirthday, adminName, adminAuthorization);

    }

    public static class AdminResult{
        private boolean success;
        private String message;
        private Admin admin;


    }

}
