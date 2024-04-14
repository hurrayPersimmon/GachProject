package com.f2z.gach.Repository;

import com.f2z.gach.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashMap;
import java.util.Map;

public interface AdminRepository extends JpaRepository <Admin, Long> {
    Admin findByAdminNum(Integer adminNum);
    Admin findByAdminName(String username);
}
