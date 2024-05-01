package com.f2z.gach.User.Service;

import com.f2z.gach.Response.ResponseEntity;
import com.f2z.gach.User.DTO.UserGuestRequestDTO;
import com.f2z.gach.User.DTO.UserGuestResponseDTO;
import com.f2z.gach.User.DTO.UserRequestDTO;
import com.f2z.gach.User.DTO.UserResponseDTO;


public interface UserService {

    ResponseEntity<UserResponseDTO.respondUsername> duplicateCheck(String username);

    ResponseEntity<UserResponseDTO.respondUserId> signUpUser(UserRequestDTO.UserDetailInfo userDetailInfo) throws Exception;

    ResponseEntity<UserResponseDTO.respondUserId> loginUser(UserRequestDTO.UserLoginInfo userLoginInfo) throws Exception;

    ResponseEntity<UserResponseDTO.respondUserId> deleteUser(Long userId) throws Exception;

    ResponseEntity<UserResponseDTO.respondUserId> updateUserDetailInfo(Long userId, UserRequestDTO.UserDetailInfo userDetailInfo) throws Exception;

    ResponseEntity<UserResponseDTO.provideUserDetailInfo> getUserDetailInfo(Long userId) throws Exception;

    ResponseEntity<UserResponseDTO.respondUserId> checkUserPassword(UserRequestDTO.UserLoginInfo userLoginInfo);

    ResponseEntity<UserGuestResponseDTO.respondGuestId> saveGuestUser(UserGuestRequestDTO.UserGuestRequest userGuestRequest);
}
