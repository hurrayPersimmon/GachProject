package com.f2z.gach.Admin;

import com.f2z.gach.EnumType.Authorization;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminDTO {

    private Integer adminNum;
    @NotBlank(message = "아이디를 입력해주세요.")
    private String adminId;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String adminPassword;
    @NotBlank(message = "비밀번호를 확인해주세요.")
    private String adminPasswordCheck;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate adminBirthday;
    @NotBlank(message = "이름을 입력해주세요.")
    private String adminName;
    @Enumerated(EnumType.STRING)
    private Authorization adminAuthorization;

    public static class AdminLoginRequest{
        @NotBlank(message = "아이디를 입력해주세요.")
        private String adminId;
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String adminPassword;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class AdminSignUpRequest{
        @NotBlank(message = "아이디를 입력해주세요.")
        private String adminId;
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String adminPassword;
        @NotBlank(message = "비밀번호를 확인해주세요.")
        private String adminPasswordCheck;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate adminBirthday;
        @NotBlank(message = "이름을 입력해주세요.")
        private String adminName;
        @Enumerated(EnumType.STRING)
        private Authorization adminAuthorization;
    }

    public Admin toEntity(AdminSignUpRequest adminSignUpRequest){
        return Admin.builder()
                .adminId(adminSignUpRequest.getAdminId())
                .adminPassword(adminSignUpRequest.getAdminPassword())
                .adminBirthday(adminSignUpRequest.getAdminBirthday())
                .adminName(adminSignUpRequest.getAdminName())
                .adminAuthorization(adminSignUpRequest.getAdminAuthorization())
                .build();
    }

    public static class AdminResult{
        private boolean success;
        private String message;
        private Admin admin;


    }

}
