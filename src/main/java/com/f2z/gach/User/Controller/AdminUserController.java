package com.f2z.gach.User.Controller;

import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.EnumType.Gender;
import com.f2z.gach.EnumType.Speed;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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


    @ModelAttribute
    public void setAttributes(Model model){
        model.addAttribute("waiterListSize", adminRepository.findByAdminAuthorization(Authorization.WAITER).size());
        model.addAttribute("inquiryWaitSize", inquiryRepository.countByInquiryProgressIsFalse());
    }

    @GetMapping("/users/list/{page}")
    public String userList(Model model, @PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 10, Sort.by("userId").descending());
        Page<User> users = userRepository.findAll(pageable);
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
    public String userUpdate(@ModelAttribute("userForm") UserForm userForm){
        User user = userRepository.findByUsername(userForm.getUsername());
        user.setUserForm(userForm);
        userRepository.save(user);
        return "redirect:/admin/users/list";
    }

    @GetMapping("/users/delete/{userId}")
    public String userDelete(@PathVariable String userId){
        User user = userRepository.findByUsername(userId);
        userRepository.delete(user);
        log.info(user.toString());
        return "redirect:/admin/users/list";
    }


}
