package com.f2z.gach.User.Repository;

import com.f2z.gach.User.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByUserId(Long userId);
    Boolean existsByUsername(String username);

    boolean existsByUserId(Long userId);

    @Query("SELECT p FROM User p WHERE p.userNickname LIKE %?1%")
    Page<User> findAllByUserNicknameContaining(String sort, Pageable pageable);
}
