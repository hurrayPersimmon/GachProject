package com.f2z.gach.User.Controller;

import com.f2z.gach.User.DTO.UserGuestRequestDTO;
import com.f2z.gach.User.DTO.UserGuestResponseDTO;
import com.f2z.gach.User.DTO.UserRequestDTO;
import com.f2z.gach.User.DTO.UserResponseDTO;
import com.f2z.gach.Response.ResponseEntity;
import com.f2z.gach.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRestController {
    private final UserService userService;

    @GetMapping("/signup")
    public ResponseEntity<UserResponseDTO.respondUsername> duplicateCheck(
            @RequestParam String username) throws Exception {
        return userService.duplicateCheck(username);
    }


    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO.respondUserId> signUpUser(
            @RequestBody UserRequestDTO.UserDetailInfo userDetailInfo) throws Exception {
        return userService.signUpUser(userDetailInfo);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO.respondUserId> loginUser(
            @RequestBody UserRequestDTO.UserLoginInfo userCommonInfo) throws Exception {
        return userService.loginUser(userCommonInfo);
    }

    /*
     * 회원 정보 삭제
     */
    @DeleteMapping ("/{userId}")
    public ResponseEntity<UserResponseDTO.respondUserId> deleteUser(
            @PathVariable Long userId) throws Exception {
        return userService.deleteUser(userId);
    }

    /*
     * 회원 세부 정보 수정
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDTO.respondUserId> updateUserDetailInfo(
            @PathVariable Long userId,
            @RequestBody UserRequestDTO.UserDetailInfo userDetailInfo) throws Exception {
        return userService.updateUserDetailInfo(userId, userDetailInfo);
    }

    /*
    * 회원 세부 정보 조회
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO.provideUserDetailInfo> getUserDetailInfo(
            @PathVariable Long userId) throws Exception {
        return userService.getUserDetailInfo(userId);
    }

    @PostMapping("/check-password")
    public ResponseEntity<UserResponseDTO.respondUserId> CheckUserPassword(
            @RequestBody UserRequestDTO.UserLoginInfo userLoginInfo) throws Exception {
        return userService.checkUserPassword(userLoginInfo);
    }

    @PostMapping("/guest")
    public ResponseEntity<UserGuestResponseDTO.respondGuestId> saveGuestUser(
            @RequestBody UserGuestRequestDTO.UserGuestRequest userGuestRequest
            ) throws Exception {
        return userService.saveGuestUser(userGuestRequest);
    }








}
