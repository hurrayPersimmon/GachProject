package com.f2z.gach.Admin;

import com.f2z.gach.EnumType.Authorization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository <Admin, Long> {
    Admin findByAdminNum(Integer adminNum);
    Admin findByAdminName(String username);
    Admin findAllByAdminAuthorization(Authorization adminAuthorization);

    Admin findByAdminId(String adminId);
}
