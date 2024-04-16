package com.f2z.gach.Controller;

import com.f2z.gach.DTO.AdminDTO;
import com.f2z.gach.Entity.Admin;
import com.f2z.gach.Entity.EnumType.Authorization;
import com.f2z.gach.Repository.AdminRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
@SessionAttributes
public class AdminController {
    private final AdminRepository adminRepository;


    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("adminDto", new AdminDTO());
        return "sign-up";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute AdminDTO adminDTO){
        log.info("admin signup");
        Admin admin = adminRepository.findByAdminId(adminDTO.getAdminId());
        if(admin != null){
//            model.addAttribute("message", "이미 존재하는 아이디입니다.");
            return "redirect:/admin/signup";
        }
        adminDTO.setAdminAuthorization(Authorization.WAITER);
        adminRepository.save(adminDTO.toEntity());
//        model.addAttribute("message", "회원가입이 완료되었습니다. 다시 로그인 하세요.");
        return "redirect:/admin/login";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("adminDto", new AdminDTO());
        return "log-in";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute AdminDTO adminDto){
        log.info("login");

        Admin admin = adminRepository.findByAdminName(adminDto.getAdminId());
        if(admin == null){
//            model.addAttribute("message", "아이디가 존재하지 않습니다.");
            return "redirect:/admin/login";
        }
        if(!admin.getAdminPassword().equals(adminDto.getAdminPassword())){
//            model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
            return "redirect:/admin/login";
        }
        if(admin.getAdminAuthorization().equals(Authorization.WAITER)){
            return "redirect:/waiter";
        } else if (admin.getAdminAuthorization().equals(Authorization.GUEST)) {
            return "redirect:/dashboard-guest";
        } else{
//            model.addAttribute("admin", admin);
            return "redirect:/dashboard";
        }

    }



    @GetMapping("/main-page")
    public String test(Model model){
        return "main-page";
    }

//    @PostMapping("/login")
//    public String login(@ModelAttribute @Valid Admin admin,
//                        BindingResult result,
//                        Model model){
//        if(result.hasErrors()){
//            model.addAttribute("errors", result.getAllErrors());
//            return "admin_login";
//        }
//        else {
//            admin.setAdminAuthorization(Authorization.valueOf("WAITER"));
//            adminRepository.save(admin);
//            model.addAttribute("number",adminRepository.getAdminSize());
//            return "admin_info";
//        }
//    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleError404(HttpServletRequest request, Exception e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("status", 404);
        mav.addObject("message", "요청하신 페이지를 찾을 수 없습니다.");
        mav.addObject("url", request.getRequestURL());
        mav.setViewName("error/404");
        log.info("404 error");
        return mav;
    }
}
