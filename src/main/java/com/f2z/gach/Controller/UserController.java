package com.f2z.gach.Controller;

import com.f2z.gach.DTO.User.UserDTO;
import com.f2z.gach.DTO.User.UserGuestDTO;
import com.f2z.gach.DTO.User.UserInfoDTO;
import com.f2z.gach.Entity.User.User;
import com.f2z.gach.Entity.User.UserInfo;
import com.f2z.gach.Response.ResponseEntity;
import com.f2z.gach.Service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;


    //가천대 로그인 요청
    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@RequestBody UserDTO userDto) {
        try{
            User user= userService.loginMember(userDto);
            return new ResponseEntity<>(true, HttpStatus.OK, "login success", user);
        } catch (Exception error) {
            log.info("login error : " + error.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST, error.getMessage(), error);
        }
    }

    // 최초 로그인 회원 정보 기재 요청
    @PostMapping("/{userId}")
    public ResponseEntity<?> saveMemberInfo(@PathVariable("userId") Long userId, @RequestBody UserInfoDTO userInfoDTO) {
        try{
            Long userCode = userService.saveMemberInfo(userId, userInfoDTO);
            return new ResponseEntity<>(true, HttpStatus.OK, "info success", userCode);
        } catch (Exception error) {
            log.info("info error : " + error.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST, error.getMessage(), error);
        }
    }

    // 최초 비회원 로그인 및 정보 기재 요청
    @PostMapping("/guest")
    public ResponseEntity<?> saveGuestInfo(@RequestBody UserGuestDTO userGuestDTO) {
        try{
            Integer guestId = userService.saveGuestInfo(userGuestDTO);
            return new ResponseEntity<>(true, HttpStatus.OK, "info success", guestId);
        } catch (Exception error) {
            log.info("info error : " + error.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST,error.getMessage(), error);
        }
    }

    // 회원 정보 수정 요청
    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateMemberInfo(@PathVariable("userId") Long userId, @RequestBody UserInfoDTO userInfoDTO) {
        try{
            Long userCode = userService.updateMemberInfo(userId, userInfoDTO);
            return new ResponseEntity<>(true, HttpStatus.OK, "update success", userCode);
        } catch (Exception error) {
            log.info("update error : " + error.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST, error.getMessage(), error);

        }
    }

    // 회원 정보 삭제 요청
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteMember(@PathVariable("userId") Long userId) {
        try{
            userService.deleteMember(userId);
            return new ResponseEntity<>(true, HttpStatus.OK, "delete success", userId);
        } catch (Exception error) {
            log.info("delete error : " + error.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST, error.getMessage(), error);

        }
    }



}
