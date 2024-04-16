package com.f2z.gach.Entity;

import com.f2z.gach.Entity.EnumType.Authorization;
import com.f2z.gach.Entity.EnumType.Departments;
import com.f2z.gach.Entity.EnumType.Gender;
import com.f2z.gach.Entity.EnumType.Speed;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
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
//    @NotBlank(message = "아이디를 입력해주세요.")
    private String adminId;
//    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String adminPassword;
//    @NotBlank(message = "생년월일을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate adminBirthday;
//    @NotBlank(message = "이름을 입력해주세요.")
    private String adminName;
    @Enumerated(EnumType.STRING)
    private Authorization adminAuthorization;



}
