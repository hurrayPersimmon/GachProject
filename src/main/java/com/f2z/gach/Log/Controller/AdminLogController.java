package com.f2z.gach.Log.Controller;

import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import com.f2z.gach.Log.DTO.LogDTO;
import com.f2z.gach.Log.Entity.Log;
import com.f2z.gach.Log.Repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminLogController {
    private final LogRepository logRepository;
    private final AdminRepository adminRepository;
    private final InquiryRepository inquiryRepository;

    @ModelAttribute
    public void setAttributes(Model model){
        model.addAttribute("waiterListSize", adminRepository.findByAdminAuthorization(Authorization.WAITER).size());
        model.addAttribute("inquiryWaitSize", inquiryRepository.countByInquiryProgressIsFalse());
    }

    @GetMapping("/log/manage/{page}")
    public String manage(Model model, @PathVariable Integer page) {
        Pageable pageable = PageRequest.ofSize(10).withSort(Sort.Direction.DESC, "createDt").withPage(page);
        Page<Log> logPage = logRepository.findAll(pageable);
        List<LogDTO.LogListStructure> logList = logPage.getContent().stream()
                .map(LogDTO.LogListStructure::toLogListStructure)
                .toList();
        model.addAttribute("logs", LogDTO.toAdminlogList(logPage, logList));
        return "log/log-manage";
    }
}
