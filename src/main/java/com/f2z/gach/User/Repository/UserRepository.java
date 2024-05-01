package com.f2z.gach.User.Repository;

import com.f2z.gach.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByUserId(Long userId);
    Boolean existsByUsername(String username);
}
