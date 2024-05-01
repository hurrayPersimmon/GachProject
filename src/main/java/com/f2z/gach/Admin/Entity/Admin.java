package com.f2z.gach.Admin.Entity;

import com.f2z.gach.Admin.DTO.AdminForm;
import com.f2z.gach.EnumType.Authorization;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
@DynamicUpdate
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


    public AdminForm getAdminForm() {
        return new AdminForm(this.getAdminNum(), this.getAdminId(), this.getAdminAuthorization(), this.getAdminName(), this.getAdminBirthday());
    }

    public void setUpdate(AdminForm form){
        this.setAdminId(form.getAdminId());
        this.setAdminName(form.getAdminName());
        this.setAdminBirthday(form.getAdminBirthday());
        this.setAdminAuthorization(form.getAdminAuthorization());
    }
}
