package com.f2z.gach.Admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@Setter
public class loginDTO {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String adminId;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String adminPassword;
}
