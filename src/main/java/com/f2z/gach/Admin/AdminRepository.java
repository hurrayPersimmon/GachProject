package com.f2z.gach.Admin;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository <Admin, Long> {
    Admin findByAdminNum(Integer adminNum);
    Admin findByAdminName(String username);

    Admin findByAdminId(String adminId);
}
