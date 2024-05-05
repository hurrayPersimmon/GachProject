package com.f2z.gach.User.Entity;

import com.f2z.gach.EnumType.Gender;
import com.f2z.gach.EnumType.Speed;
import com.f2z.gach.User.DTO.UserForm;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "`USER`") // 백틱으로 감싼 user 테이블 이름
public class User {
    @Id
    @Tsid
    private Long userId;
    private String username;
    private String password;

//    @Enumerated(EnumType.STRING)
//    private Departments userDepartment;
    private String userNickname;
    @Enumerated(EnumType.ORDINAL)
    private Speed userSpeed;
    @Enumerated(EnumType.STRING)
    private Gender userGender;
    private Integer userBirth;
    private Double userHeight;
    private Double userWeight;

    public void updateUserInfo(User user){
//        this.userDepartment = user.getUserDepartment();
        if(user.getPassword() !=null) this.password = user.getPassword();
        this.userNickname = user.getUserNickname();
        this.userSpeed = user.getUserSpeed();
        this.userGender = user.getUserGender();
        this.userBirth = user.getUserBirth();
        this.userHeight = user.getUserHeight();
        this.userWeight = user.getUserWeight();
    }

    public UserForm getUserForm(){
        return new UserForm(this.username, this.userNickname, this.userBirth, this.userHeight,
                this.userWeight, this.userSpeed, this.userGender);
    }

    public void setUserForm(UserForm userForm){
        this.username = userForm.getUsername();
        this.userNickname = userForm.getUserNickname();
        this.userGender = userForm.getUserGender();
        this.userBirth = userForm.getUserBirth();
        this.userHeight = userForm.getUserHeight();
        this.userSpeed = userForm.getUserSpeed();
        this.userWeight = userForm.getUserWeight();
    }
}
