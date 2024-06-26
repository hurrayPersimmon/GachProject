package com.f2z.gach.Admin.Entity;

import com.f2z.gach.Admin.DTO.AdminForm;
import com.f2z.gach.EnumType.Authorization;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
@DynamicUpdate
public class Admin implements UserDetails {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(adminAuthorization.getValue()));
    }



    @Override
    public String getPassword() {
        return adminPassword;
    }

    @Override
    public String getUsername() {
        return adminId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
