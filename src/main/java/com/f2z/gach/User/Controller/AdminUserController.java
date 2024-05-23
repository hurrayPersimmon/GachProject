package com.f2z.gach.User.Controller;

import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.EnumType.Gender;
import com.f2z.gach.EnumType.LogLevel;
import com.f2z.gach.EnumType.Speed;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import com.f2z.gach.Log.Repository.LogRepository;
import com.f2z.gach.User.DTO.UserForm;
import com.f2z.gach.User.DTO.UserResponseDTO;
import com.f2z.gach.User.Entity.User;
import com.f2z.gach.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
@SessionAttributes
public class AdminUserController {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final InquiryRepository inquiryRepository;
    private final LogRepository logRepository;

    @ModelAttribute
    public void setAttributes(Model model){
        model.addAttribute("waiterListSize", adminRepository.findByAdminAuthorization(Authorization.WAITER).size());
        model.addAttribute("inquiryWaitSize", inquiryRepository.countByInquiryProgressIsFalse());
    }

    @GetMapping("/users/list/{page}")
    public String userList(Model model, @PathVariable Integer page){
        Pageable pageable = PageRequest.ofSize(10).withSort(Sort.Direction.DESC, "userId").withPage(page);
        Page<User> users = userRepository.findAll(pageable);
        List<UserResponseDTO.UserListStructure> userList = users.getContent().stream()
                .map(UserResponseDTO.UserListStructure::toUserListResponseDTO).toList();
        model.addAttribute("userList", UserResponseDTO.toUserResponseList(users, userList));
        model.addAttribute("userChartData", userRepository.findAll());
        model.addAttribute("userSignUpCount", logRepository.countLogsGroupedByDateAndUrlPattern(
                LocalDateTime.now().minusDays(4).with(LocalDateTime.MIN),
                LocalDateTime.now().with(LocalDateTime.MAX),
                "POST", "/user/signup%"
        ));
        log.info(String.valueOf(logRepository.countLogsByDateAndUrl(
                LocalDateTime.now().minusDays(4).with(LocalDateTime.MIN),
                LocalDateTime.now().with(LocalDateTime.MAX),
                "POST", "/user/signup"
        )));
        model.addAttribute("userDeleteCnt", logRepository.countLogsByDateRangeAndUrl(
                LocalDateTime.now().minusYears(1).with(LocalTime.MIN),
                LocalDateTime.now().with(LocalTime.MAX),
                "DELETE", "/user"));
        model.addAttribute("userLoginCnt", logRepository.countLogsByDateRangeAndUrl(
                LocalDateTime.now().minusDays(4).with(LocalDateTime.MIN),
                LocalDateTime.now().with(LocalDateTime.MAX),
                "POST", "/user/login"
        ));
        log.info(String.valueOf(logRepository.countLogsByDateAndUrl(
                LocalDateTime.now().minusDays(4).with(LocalDateTime.MIN),
                LocalDateTime.now().with(LocalDateTime.MAX),
                "POST", "/user/login"
        )));
        return "user/user-manage";
    }

    @GetMapping("/users/sortedlist/{page}")
    public String userListSortedPage(Model model, @PathVariable Integer page, @RequestParam String sort){
        Pageable pageable = PageRequest.ofSize(10).withSort(Sort.Direction.DESC, "userId").withPage(page);
        Page<User> users = userRepository.findAllByUserNicknameContaining(sort, pageable);
        List<UserResponseDTO.UserListStructure> userList = users.getContent().stream()
                .map(UserResponseDTO.UserListStructure::toUserListResponseDTO).toList();
        model.addAttribute("userList", UserResponseDTO.toUserResponseList(users, userList));
        return "user/user-manage";
    }

    @GetMapping("/users/{userId}")
    public String userDetail(Model model, @PathVariable Long userId){
        UserForm user = userRepository.findByUserId(userId).getUserForm();
        log.info(user.toString());
        model.addAttribute("userForm", user);
        model.addAttribute("gender", Gender.values());
        model.addAttribute("speed", Speed.values());
        log.info(user.toString());
        return "user/user-detail";
    }

    @PostMapping("/users/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String userUpdate(@ModelAttribute("userForm") UserForm userForm){
        User user = userRepository.findByUserId(userForm.getUserId());
        log.info(userForm.toString());
        user.setUserForm(userForm);

        userRepository.save(user);
        return "redirect:/admin/users/list/0";
    }

    @GetMapping("/users/delete/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String userDelete(@PathVariable Long userId){
        User user = userRepository.findByUserId(userId);
        userRepository.delete(user);
        log.info(user.toString());
        return "redirect:/admin/users/list/0";
    }


}
