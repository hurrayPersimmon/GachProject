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
import java.util.Random;

@RequiredArgsConstructor
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Random random = new Random();

        for(int i = 0; i < 30; i++){
            Admin admin = new Admin();
            admin.setAdminId("guest" + i+1);
            admin.setAdminName("게스트" + i+1);
            admin.setAdminBirthday(LocalDate.of(2000, random.nextInt(10), 1).plusDays(random.nextInt(30)));
            admin.setAdminPassword(passwordEncoder.encode("1234"));
            admin.setAdminAuthorization(Authorization.GUEST);
            adminRepository.save(admin);
        }
    }
}
