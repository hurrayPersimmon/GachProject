package com.f2z.gach.Admin;

import com.f2z.gach.EnumType.Authorization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository <Admin, Long> {
    Admin findByAdminNum(Integer adminNum);
    Admin findByAdminName(String username);

    List<Admin> findByAdminAuthorization(Authorization adminAuthorization);

    Admin findByAdminId(String adminId);
}
