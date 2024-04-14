package com.f2z.gach.Entity.User;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "`USER`") // 백틱으로 감싼 user 테이블 이름
public class User {
    @Id
    @Tsid
    private Long userId;
    private String username;
    private String password;


    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private UserInfo userInfo;


    public User(Long userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }
}
