package com.f2z.gach.Auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class AuthController {

    @GetMapping("/admin/deny")
    public String deny() {
        return "deny";
    }
}
