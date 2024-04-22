package com.f2z.gach.User;

import com.f2z.gach.User.Entities.User;
import com.f2z.gach.User.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
@SessionAttributes
public class AdminUserController {
    private final UserRepository userRepository;

    @GetMapping("/users/list")
    public String userList(Model model){
        model.addAttribute("userList", userRepository.findAll());
        return "user-manage";

    }

    @GetMapping("/users/{userId}")
    public String userDetail(Model model, Long userId){
        model.addAttribute("user", userRepository.findById(userId));
        return "user-detail";
    }

    @PostMapping("/users/{userId}")
    public String userUpdate(@PathVariable Long userId, @ModelAttribute("user") User user){
        User userEntity = userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("해당 사용자가 없습니다.")
        );
        userEntity.updateUserInfo(user);
        return "redirect:/admin/users/list";
    }

    @GetMapping("/users/delete/{userId}")
    public String userDelete(@PathVariable Long userId){
        userRepository.deleteById(userId);
        return "redirect:/admin/users/list";
    }


}
