package com.f2z.gach.DTO.User;

import com.f2z.gach.Entity.User.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private Long userId;
    private String username;
    private String password;

    public UserDTO(Long userId) {
        this.userId = userId;
    }

    public User toEntity() {
        return new User(userId, username, password);
    }


}
