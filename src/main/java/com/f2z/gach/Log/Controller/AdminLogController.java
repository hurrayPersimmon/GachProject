package com.f2z.gach.Log.Controller;

import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import com.f2z.gach.Log.Repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminLogController {
    private final LogRepository logRepository;
    private final AdminRepository adminRepository;
    private final InquiryRepository inquiryRepository;

    @ModelAttribute
    public void setAttributes(Model model){
        model.addAttribute("waiterListSize", adminRepository.findByAdminAuthorization(Authorization.WAITER).size());
        model.addAttribute("inquiryWaitSize", inquiryRepository.countByInquiryProgressIsFalse());
    }

    @GetMapping("/log/manage")
    public String manage(Model model) {
        model.addAttribute("logs", logRepository.findAll());
        return "log/log-manage";
    }
}
