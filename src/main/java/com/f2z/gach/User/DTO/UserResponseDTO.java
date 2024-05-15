package com.f2z.gach.User.DTO;

import com.f2z.gach.EnumType.Gender;
import com.f2z.gach.EnumType.Speed;
import com.f2z.gach.History.DTO.HistoryResponseDTO;
import com.f2z.gach.User.Entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;


public class UserResponseDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class respondUserId {
        private Long userId;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class respondUsername {
        private String username;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class provideUserDetailInfo {
        private Long userId;
//        @Enumerated(EnumType.STRING)
//        private Departments userDepartment;
        private String username;
        private String userNickname;
        @Enumerated(EnumType.ORDINAL)
        private Speed userSpeed;
        @Enumerated(EnumType.STRING)
        private Gender userGender;
        private Integer userBirth;
        private Double userHeight;
        private Double userWeight;
    }

    public static provideUserDetailInfo toProvideUserDetailInfo(User user) {
        return provideUserDetailInfo.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .userNickname(user.getUserNickname())
                .userSpeed(user.getUserSpeed())
                .userGender(user.getUserGender())
                .userBirth(user.getUserBirth())
                .userHeight(user.getUserHeight())
                .userWeight(user.getUserWeight())
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserListStructure {
        private Long userId;
        private String username;
        private String userNickname;
        private Gender userGender;
        private Integer userBirth;
        private Double userHeight;
        private Double userWeight;

        public static UserListStructure toUserListResponseDTO(User user) {
            return UserListStructure.builder()
                    .userId(user.getUserId())
                    .username(user.getUsername())
                    .userNickname(user.getUserNickname())
                    .userGender(user.getUserGender())
                    .userBirth(user.getUserBirth())
                    .userHeight(user.getUserHeight())
                    .userWeight(user.getUserWeight())
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserList{
        List<UserListStructure> userList;
        Integer totalPage;
        Long totalElements;
    }

    public static UserList toUserResponseList(Page<User> userPages, List<UserListStructure> userlist){
        return UserList.builder()
                .userList(userlist)
                .totalPage(userPages.getTotalPages())
                .totalElements(userPages.getTotalElements())
                .build();
    }

}
