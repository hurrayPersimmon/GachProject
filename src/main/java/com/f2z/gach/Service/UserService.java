package com.f2z.gach.Service;

import com.f2z.gach.DTO.User.UserDTO;
import com.f2z.gach.DTO.User.UserGuestDTO;
import com.f2z.gach.DTO.User.UserInfoDTO;
import com.f2z.gach.Entity.User.User;
import com.f2z.gach.Entity.User.UserGuest;
import com.f2z.gach.Entity.User.UserInfo;
import com.f2z.gach.Repository.User.UserGuestRepository;
import com.f2z.gach.Repository.User.UserInfoRepository;
import com.f2z.gach.Repository.User.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Array;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserGuestRepository userGuestRepository;
    public User loginMember(UserDTO userDto) throws Exception {
        if(userRepository.existsByUsername(userDto.getUsername())) {
            try {
                checkPassword(userDto);
                return userRepository.findByUsername(userDto.getUsername());
            } catch (Exception error) {
                throw new Exception(error);
            }
        } else {
            try {
                return gachonLogin(userDto);
            } catch (Exception error) {
                throw new Exception(error);
            }
        }
    }

    private User gachonLogin(UserDTO userDto) throws Exception{
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://smart.gachon.ac.kr:8080/WebJSON";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        org.json.JSONObject jsonObject= new org.json.JSONObject();

        jsonObject.put("fsp_cmd","login");
        jsonObject.put("DVIC_ID","dwFraM1pVhl6mMn4npgL2dtZw7pZxw2lo2uqpm1yuMs=");
        jsonObject.put("fsp_action","UserAction");
        jsonObject.put("USER_ID",username); //user ID 사용자에게 받은 값 입력
        log.info(username);
        jsonObject.put("PWD",password); //user PW 사용자에게 받은 값 입력
        jsonObject.put("APPS_ID","com.sz.Atwee.gachon");

        HttpEntity<String> requestMessage = new HttpEntity<>(jsonObject.toString(), httpHeaders); //Request&Header Setting
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestMessage, String.class); //호출 하여 Response 받기

        JSONObject data = new JSONObject(response.getBody());

        if(data.getString("ErrorCode").equals("0")) {
            //신규 유저, DB에 저장
            //password 암호화 함수 호출 후 저장
            userDto.setPassword(encodePassword(userDto.getPassword()));
            userRepository.save(userDto.toEntity());
            log.info("신규 유저 저장 완료");
            return userRepository.findByUsername(username);
        }
        else {
            log.info("password 오류 : " + data.getString("ErrorCode") + " " + encodePassword(password));
            throw new Exception("아이디 혹은 비밀번호를 확인하세요");
        }

    }

    private Boolean checkPassword(UserDTO userDto) throws Exception {
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        User member = userRepository.findByUsername(username);
        if(member.getPassword().equals(encodePassword(password))) {
            log.info("기존 유저 로그인 성공");
            return true;
        }
        else {
            log.info("password 오류 - " + userDto.getUsername());
            throw new Exception("아이디 혹은 비밀번호를 확인하세요");
        }
    }

    private static String encodePassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes());

        StringBuilder hexString = new StringBuilder();
        for(byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


    public Long saveMemberInfo(Long userId, UserInfoDTO userInfoDTO) throws Exception {
        if(userRepository.existsById(userId)) {
            try {
                UserInfo userInfo = userInfoDTO.toEntity();
                userInfo.setUserCode(userId);
                userInfoRepository.save(userInfo);
                return userInfo.getUserCode();
            } catch (Exception error) {
                throw new Exception(error);
            }
        } else {
            throw new Exception("해당 유저가 없습니다");
        }
        
    }

    public Integer saveGuestInfo(UserGuestDTO userGuestDTO) {
        try {
            UserGuest guest = userGuestDTO.toEntity();
            userGuestRepository.save(guest);
            return guest.getGuestId();
        } catch (Exception error) {
            log.info("guest error : " + error.getMessage());
            return null;
        }
    }

    public Long updateMemberInfo(Long userId, UserInfoDTO userInfoDTO) throws Exception {
        if(userRepository.existsById(userId)) {
            try {
                UserInfo userInfo = userInfoDTO.toEntity();
                userInfo.setUserCode(userId);
                userInfoRepository.save(userInfo);
                return userInfo.getUserCode();
            } catch (Exception error) {
                throw new Exception(error);
            }
        } else {
            throw new Exception("해당 유저가 없습니다");
        }
    }

    public void deleteMember(Long userId) throws Exception {
        if(userRepository.existsById(userId)) {
            try {
                userRepository.deleteById(userId);
            } catch (Exception error) {
                throw new Exception(error);
            }
        } else {
            throw new Exception("해당 유저가 없습니다");
        }
    }


//    public Object ExistUserInfo(String username) {
//        boolean isExistUserInfo = userInfoRepository.existsByUserCode(userRepository.findByUsername(username).getUserId());
//        return new Array() {
//            isExistUserInfo
//        }
//    }
}
