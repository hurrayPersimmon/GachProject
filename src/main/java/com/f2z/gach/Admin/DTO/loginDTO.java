package com.f2z.gach.Admin.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class loginDTO {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String adminId;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String adminPassword;
}
