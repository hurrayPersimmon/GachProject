package com.f2z.gach.Repository.User;

import com.f2z.gach.Entity.User.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    boolean existsByUserCode(Long userCode);
}
