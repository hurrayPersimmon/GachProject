package com.f2z.gach.Controller;

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

    @PostMapping("/signup")
    public String login(@ModelAttribute @Valid Admin admin,
                        BindingResult result,
                        Model model){
        if(result.hasErrors()){
            model.addAttribute("errors", result.getAllErrors());
            return "admin_login";
        }
        else {
            admin.setAdminAuthorization(Authorization.valueOf("WAITER"));
            adminRepository.save(admin);
            model.addAttribute("number",adminRepository.getAdminSize());
            return "admin_info";
        }
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
