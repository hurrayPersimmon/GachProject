package com.f2z.gach.Admin.Controller;

import com.f2z.gach.Admin.DTO.AdminDTO;
import com.f2z.gach.Admin.DTO.AdminForm;
import com.f2z.gach.Admin.DTO.loginDTO;
import com.f2z.gach.Admin.Entity.Admin;
import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.Auth.CustomPasswordEncoder;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.EnumType.InquiryCategory;
import com.f2z.gach.EnumType.LogLevel;
import com.f2z.gach.History.Entity.UserHistory;
import com.f2z.gach.History.Repository.UserHistoryRepository;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import com.f2z.gach.Map.Entity.MapNode;
import com.f2z.gach.User.Repository.UserRepository;
import com.f2z.gach.Log.Repository.LogRepository;
import com.f2z.gach.User.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
@SessionAttributes
public class AdminController {
    private final AdminRepository adminRepository;
    private final InquiryRepository inquiryRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final LogRepository logRepository;

    @ModelAttribute
    public void setAttributes(Model model){
        model.addAttribute("waiterListSize", adminRepository.findByAdminAuthorization(Authorization.WAITER).size());
        model.addAttribute("inquiryWaitSize", inquiryRepository.countByInquiryProgressIsFalse());
    }

    @GetMapping("/main-page")
    public String mainPage(){
        return "main/main-page";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("userTodaySignUpCount", logRepository.countBySpecificConditions(
                LocalDateTime.now().with(LocalTime.MIN),
                LocalDateTime.now().with(LocalTime.MAX),
                "POST", LogLevel.INFO, "/user/signup"));
        model.addAttribute("requestTodayCount", logRepository.countLogsCreatedToday(
                LocalDateTime.now().with(LocalTime.MIN),
                LocalDateTime.now().with(LocalTime.MAX)));
        model.addAttribute("requestErrorTodayCount", logRepository.countByLogLevel(
                LocalDateTime.now().with(LocalTime.MIN),
                LocalDateTime.now().with(LocalTime.MAX),
                LogLevel.ERROR));
        model.addAttribute("inquiryRequest", inquiryRepository.findAll());
        model.addAttribute("inquiryAr", inquiryRepository.findAllByInquiryCategory(InquiryCategory.AR));
        model.addAttribute("inquiryAI", inquiryRepository.findAllByInquiryCategory(InquiryCategory.AITime));
        model.addAttribute("top5Nodes" ,userHistoryRepository.findTopMapNodes(5));

        return "main/dashboard";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("loginDto", new loginDTO());
        return "main/log-in";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginDto") loginDTO adminDto, BindingResult result,
                        Model model){
        if(result.hasErrors()){
            return "main/log-in";
        }

        Admin admin = adminRepository.findByAdminId(adminDto.getAdminId());
        if(admin == null){
            model.addAttribute("idMessage", "아이디가 존재하지 않습니다.");
            return "main/log-in";
        }
        if(!admin.getAdminPassword().equals(adminDto.getAdminPassword())){
            model.addAttribute("message", "비밀번호가 다릅니다.");
            return "main/log-in";
        }
        if(admin.getAdminAuthorization().equals(Authorization.WAITER)){
            return "waiter";
        } else if (admin.getAdminAuthorization().equals(Authorization.GUEST)) {
            log.info("게스트");
            return "dashboard-guest";
        } else{
            log.info("관리자");
            return "redirect:/admin/node/list/0";
        }
    }

    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("adminDto", new AdminDTO());
        return "main/sign-up";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("adminDto") AdminDTO adminDto, BindingResult result, Model model){
        if(result.hasErrors()){
            return "main/sign-up";
        } else if(!adminDto.getAdminPassword().equals(adminDto.getAdminPasswordCheck())){
            model.addAttribute("passMessage", "비밀번호가 다릅니다.");
            return "main/sign-up"; //비밀번호가 다른 경우
        }
        Admin admin = adminRepository.findByAdminId(adminDto.getAdminId());

        if(admin != null){
            model.addAttribute("idMessage", "이미 존재하는 아이디입니다.");
            return "main/sign-up";
        }

        adminDto.setAdminAuthorization(Authorization.WAITER);
        adminRepository.save(adminDto.toEntity());
        model.addAttribute("message", "회원가입이 완료되었습니다. 관리자 승인까지 잠시만 기다려주세요.");
        model.addAttribute("loginDto", new loginDTO());
        return "main/log-in";
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

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminList(Model model){

        List<Admin> admins = adminRepository.findAll();
        Collections.reverse(admins);

        model.addAttribute("adminList", admins);
        model.addAttribute("waiterList", adminRepository.findByAdminAuthorization(Authorization.WAITER));
        model.addAttribute("authList", Authorization.values());
        model.addAttribute("authAdmin", Authorization.ADMIN);
        return "admin-manage";
    }

    @GetMapping("/approve/{adminNum}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String approveAdmin(@PathVariable Integer adminNum){
        Admin admin = adminRepository.findById(Long.valueOf(adminNum)).orElseThrow();
        admin.setAdminAuthorization(Authorization.GUEST);
        adminRepository.save(admin);
        return "redirect:/admin/list";
    }

    @GetMapping("/delete/{adminId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteAdmin(@PathVariable String adminId){
        Admin admin = adminRepository.findByAdminId(adminId);
        log.info(admin.toString());
        adminRepository.delete(admin);
        return "redirect:/admin/list";
    }

    @GetMapping("/json/{adminNum}")
    @ResponseBody
    public Admin adminDetailJson(@PathVariable Integer adminNum){
        log.info("요청");
        Admin admin = adminRepository.findById(Long.valueOf(adminNum)).orElseThrow();
        log.info(admin.toString());
        return admin;
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateAdmin(AdminForm adminForm){
        log.info(adminForm.toString());

        Admin admin = adminRepository.findByAdminId(adminForm.getAdminId());
        admin.setUpdate(adminForm);
        adminRepository.save(admin);
        log.info("저장완료");
        return "redirect:/admin/list";
    }

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
