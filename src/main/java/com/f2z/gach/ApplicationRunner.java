package com.f2z.gach;

import com.f2z.gach.Admin.Entity.Admin;
import com.f2z.gach.EnumType.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Admin admin = new Admin();
        admin.setAdminAuthorization(Authorization.WAITER);
        admin.setAdminName("door");
        admin.setAdminPassword("door");
    }
}
