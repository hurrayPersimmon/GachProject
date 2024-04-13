package com.f2z.gach.Repository;

import com.f2z.gach.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository <Admin, Long> {
    Integer getAdminSize();
}
