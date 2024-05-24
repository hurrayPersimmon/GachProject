package com.f2z.gach;

import com.f2z.gach.Admin.Entity.Admin;
import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.Auth.CustomPasswordEncoder;
import com.f2z.gach.EnumType.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Admin admin = new Admin();
        admin.setAdminPassword(passwordEncoder.encode("guest1234"));
        admin.setAdminBirthday(LocalDate.now());
        admin.setAdminName("guest");
        admin.setAdminAuthorization(Authorization.GUEST);
        admin.setAdminId("admin");
        adminRepository.save(admin);
    }
}
