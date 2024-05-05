package com.f2z.gach.Admin.DTO;

import com.f2z.gach.EnumType.Authorization;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminForm {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String adminId;
    @Enumerated(EnumType.STRING)
    private Authorization adminAuthorization;
    @NotBlank(message = "이름을 입력해주세요.")
    private String adminName;
    @NotNull(message = "생년월일을 확인해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate adminBirthday;

    public AdminForm(Integer adminNum, String adminId, Authorization adminAuthorization, String adminName, LocalDate adminBirthday) {
    }
}
