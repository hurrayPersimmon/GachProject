package com.f2z.gach.User.Repository;

import com.f2z.gach.User.Entity.UserGuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGuestRepository extends JpaRepository<UserGuest, Long> {
}
