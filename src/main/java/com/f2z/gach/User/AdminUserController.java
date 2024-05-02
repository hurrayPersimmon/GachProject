package com.f2z.gach.User;

import com.f2z.gach.EnumType.Gender;
import com.f2z.gach.EnumType.Speed;
import com.f2z.gach.User.DTOs.UserForm;
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

    @GetMapping("/users/{id}")
    public String userDetail(Model model, @PathVariable String id){
        UserForm user = userRepository.findByUsername(id).getUserForm();
        log.info(user.toString());
        model.addAttribute("userForm", user);
        model.addAttribute("gender", Gender.values());
        model.addAttribute("speed", Speed.values());
        log.info(user.toString());
        return "user-detail";
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
