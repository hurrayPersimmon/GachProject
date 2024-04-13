package com.f2z.gach.Repository.User;

import com.f2z.gach.Entity.User.UserGuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGuestRepository extends JpaRepository<UserGuest, Long> {
}
