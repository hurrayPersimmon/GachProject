package com.f2z.gach.User;

import com.f2z.gach.Response.ResponseEntity;
import com.f2z.gach.User.DTOs.UserGuestRequestDTO;
import com.f2z.gach.User.DTOs.UserGuestResponseDTO;
import com.f2z.gach.User.DTOs.UserRequestDTO;
import com.f2z.gach.User.DTOs.UserResponseDTO;
import com.f2z.gach.User.Entities.User;
import com.f2z.gach.User.Entities.UserGuest;
import com.f2z.gach.User.Repositories.UserGuestRepository;
import com.f2z.gach.User.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.f2z.gach.User.DTOs.UserResponseDTO.toProvideUserDetailInfo;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserGuestRepository userGuestRepository;


    private void checkPassword(UserRequestDTO.UserLoginInfo userLoginInfo) throws Exception {
        Long userId = userLoginInfo.getUserId();
        String username = userLoginInfo.getUsername();
        String password = userLoginInfo.getPassword();
        String targetPassword = userId != null ? userRepository.findByUserId(userId).getPassword() :
                userRepository.findByUsername(username).getPassword();
//        if(member.getPassword().equals(encodePassword(password))) {
        if(targetPassword.equals(password)) {
            log.info("비밀번호가 일치합니다.");
        }
        else {
            log.info("password 오류 username : " + userLoginInfo.getUsername());
            throw new Exception("비밀번호를 확인하세요");
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

    @Override
    public ResponseEntity<UserResponseDTO.respondUsername> duplicateCheck(String username) {
        if(!userRepository.existsByUsername(username)) {
            return ResponseEntity.NotDuplicateID(username);
        } else {
            return ResponseEntity.DuplicateID(username);
        }
    }

    @Override
    public ResponseEntity<UserResponseDTO.respondUserId> signUpUser(UserRequestDTO.UserDetailInfo userDetailInfo) throws Exception {
        if(userRepository.existsByUsername(userDetailInfo.getUsername())) {
            return ResponseEntity.DuplicateID(userDetailInfo.getUsername());
        } else {
            User user = userRepository.save(UserRequestDTO.UserDetailInfo.toUserDetailInfo(userDetailInfo));
            return ResponseEntity.saveSuccess(new UserResponseDTO.respondUserId(user.getUserId()));
        }
    }

    @Override
    public ResponseEntity<UserResponseDTO.respondUserId> loginUser(UserRequestDTO.UserLoginInfo userLoginInfo) throws Exception {
        log.info("loginUser"+userLoginInfo.getUsername());
        if(userRepository.existsByUsername(userLoginInfo.getUsername())) {
            try {
                checkPassword(userLoginInfo);
                User user = userRepository.findByUsername(userLoginInfo.getUsername());
                return ResponseEntity.requestSuccess(new UserResponseDTO.respondUserId(user.getUserId()));
            } catch (Exception error) {
                return ResponseEntity.wrongPassword(null);
            }
        }else {
            log.info("아이디 오류 : " + userLoginInfo.getUsername());
            return ResponseEntity.wrongUsername(null);
        }

    }

    @Override
    public ResponseEntity<UserResponseDTO.respondUserId> deleteUser(Long userId) throws Exception {
        if (userRepository.existsById(userId)) {
            try {
                userRepository.deleteById(userId);
                return ResponseEntity.requestSuccess(new UserResponseDTO.respondUserId(userId));
            } catch (Exception error) {
                log.info("삭제 실패 : " + error.getMessage());
                throw new Exception(error);
            }
        } else {
            log.info("삭제 실패, 해당 유저가 없음.");
            return ResponseEntity.notFound(null);
        }
    }


    @Override
    public ResponseEntity<UserResponseDTO.respondUserId> updateUserDetailInfo(Long userId, UserRequestDTO.UserDetailInfo userDetailInfo) throws Exception {
        if (userRepository.existsById(userId)) {
            try {
                User target = userRepository.findByUserId(userId);
                User member = UserRequestDTO.UserDetailInfo.toUserDetailInfo(userDetailInfo);
                target.updateUserInfo(member);
                userRepository.save(target);
                return ResponseEntity.saveSuccess(new UserResponseDTO.respondUserId(userId));
            } catch (Exception error) {
                log.info("저장 실패 : " + error.getMessage());
                throw new Exception(error);
            }

        } else {
            log.info("저장 실패, 해당 유저가 없음.");
            return ResponseEntity.notFound(null);
        }
    }

    @Override
    public ResponseEntity<UserResponseDTO.provideUserDetailInfo> getUserDetailInfo(Long userId) throws Exception {
        if (userRepository.existsById(userId)) {
            try{
                User user = userRepository.findByUserId(userId);
                return ResponseEntity.requestSuccess(toProvideUserDetailInfo(user));
            } catch (Exception error) {
                log.info("회원 상세 정보 조회 실패 : " + error.getMessage());
                throw new Exception(error);
            }
        } else {
            log.info("회원 상세 정보 조회 실패, 해당 유저가 없음.");
            return ResponseEntity.notFound(null);
        }
    }

    @Override
    public ResponseEntity<UserResponseDTO.respondUserId> checkUserPassword(UserRequestDTO.UserLoginInfo userLoginInfo) {
        if(userRepository.existsById(userLoginInfo.getUserId())) {
            try {
                checkPassword(userLoginInfo);
                return ResponseEntity.requestSuccess(new UserResponseDTO.respondUserId(userLoginInfo.getUserId()));
            } catch (Exception error) {
                return ResponseEntity.wrongPassword(null);
            }
        } else {
            return ResponseEntity.notFound(null);
        }
    }

    @Override
    public ResponseEntity<UserGuestResponseDTO.respondGuestId> saveGuestUser(UserGuestRequestDTO.UserGuestRequest userGuestRequest) {
        UserGuest guestMember = userGuestRepository.save(UserGuestRequestDTO.UserGuestRequest.toEntity(userGuestRequest));
        return ResponseEntity.saveSuccess(new UserGuestResponseDTO.respondGuestId(guestMember.getGuestId()));
    }


//    public Object ExistUserInfo(String username) {
//        boolean isExistUserInfo = userInfoRepository.existsByUserCode(userRepository.findByUsername(username).getUserId());
//        return new Array() {
//            isExistUserInfo
//        }
//    }

//    public User loginMember(UserRequestDTO userRequestDTO) throws Exception {
//        if(userRepository.existsByUsername(userRequestDTO.getUsername())) {
//            try {
//                checkPassword(userRequestDTO);
//                return userRepository.findByUsername(userRequestDTO.getUsername());
//            } catch (Exception error) {
//                throw new Exception(error);
//            }
//        } else {
//            try {
//                return gachonLogin(userRequestDTO);
//            } catch (Exception error) {
//                throw new Exception(error);
//            }
//        }
//    }
//
//    private User gachonLogin(UserRequestDTO userRequestDTO) throws Exception{
//        String username = userRequestDTO.getUsername();
//        String password = userRequestDTO.getPassword();
//
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://smart.gachon.ac.kr:8080/WebJSON";
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//
//        JSONObject jsonObject= new JSONObject();
//
//        jsonObject.put("fsp_cmd","login");
//        jsonObject.put("DVIC_ID","dwFraM1pVhl6mMn4npgL2dtZw7pZxw2lo2uqpm1yuMs=");
//        jsonObject.put("fsp_action","UserAction");
//        jsonObject.put("USER_ID",username); //user ID 사용자에게 받은 값 입력
//        log.info(username);
//        jsonObject.put("PWD",password); //user PW 사용자에게 받은 값 입력
//        jsonObject.put("APPS_ID","com.sz.Atwee.gachon");
//
//        HttpEntity<String> requestMessage = new HttpEntity<>(jsonObject.toString(), httpHeaders); //Request&Header Setting
//        log.info("Request : " + requestMessage);
//        ResponseEntity<String> response = restTemplate.postForEntity(url, requestMessage, String.class); //호출 하여 Response 받기
//        log.info(String.valueOf(response));
//        JSONObject data = new JSONObject(response.getBody());
//
//        if(data.getString("ErrorCode").equals("0")) {
//            //신규 유저, DB에 저장
//            //password 암호화 함수 호출 후 저장
//            userRequestDTO.setPassword(encodePassword(userRequestDTO.getPassword()));
//            userRepository.save(userRequestDTO.toEntity());
//            log.info("신규 유저 저장 완료");
//            return userRepository.findByUsername(username);
//        }
//        else {
//            log.info("password 오류 : " + data.getString("ErrorCode") + " " + encodePassword(password));
//            throw new Exception("아이디 혹은 비밀번호를 확인하세요");
//        }
//
//    }

    //    public Long saveMemberInfo(Long userId, UserInfoRequest userInfoRequest) throws Exception {
//        if(userRepository.existsById(userId)) {
//            try {
//                UserInfo userInfo = userInfoRequest.toEntity();
//                userInfo.setUserCode(userId);
//                userInfoRepository.save(userInfo);
//                return userInfo.getUserCode();
//            } catch (Exception error) {
//                throw new Exception(error);
//            }
//        } else {
//            throw new Exception("해당 유저가 없습니다");
//        }
//
//    }
//
//    public Integer saveGuestInfo(UserGuestRequest userGuestRequest) {
//        try {
//            UserGuest guest = userGuestRequest.toEntity();
//            userGuestRepository.save(guest);
//            return guest.getGuestId();
//        } catch (Exception error) {
//            log.info("guest error : " + error.getMessage());
//            return null;
//        }
//    }
//
//    public Long updateMemberInfo(Long userId, UserInfoRequest userInfoRequest) throws Exception {
//        if(userRepository.existsById(userId)) {
//            try {
//                UserInfo userInfo = userInfoRequest.toEntity();
//                userInfo.setUserCode(userId);
//                userInfoRepository.save(userInfo);
//                return userInfo.getUserCode();
//            } catch (Exception error) {
//                throw new Exception(error);
//            }
//        } else {
//            throw new Exception("해당 유저가 없습니다");
//        }
//    }
//
//    public void deleteMember(Long userId) throws Exception {
//        if(userRepository.existsById(userId)) {
//            try {
//                userRepository.deleteById(userId);
//            } catch (Exception error) {
//                throw new Exception(error);
//            }
//        } else {
//            throw new Exception("해당 유저가 없습니다");
//        }
//    }
}
