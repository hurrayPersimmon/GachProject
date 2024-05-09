package com.f2z.gach.Inquiry.Controller;

import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.Inquiry.DTO.InquiryRequestDTO;
import com.f2z.gach.Inquiry.DTO.InquiryResponseDTO;
import com.f2z.gach.Inquiry.Entity.Inquiry;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/inquiry")
@SessionAttributes
public class AdminInquiryController {


    private final AdminRepository adminRepository;
    private final InquiryRepository inquiryRepository;


    @ModelAttribute
    public void setAttributes(Model model){
        model.addAttribute("waiterListSize", adminRepository.findByAdminAuthorization(Authorization.WAITER).size());
        model.addAttribute("inquiryWaitSize", inquiryRepository.countByInquiryProgressIsFalse());

    }

    @GetMapping("/list/{page}")
    public String inquiryListPage(Model model, @PathVariable Integer page){
        Pageable pageable = Pageable.ofSize(10).withPage(page);
        Page<Inquiry> inquiryPage = inquiryRepository.findAll(pageable);
        List<InquiryResponseDTO.InquiryListStructureForAdmin> inquiryList = inquiryPage.getContent().stream()
                .map(InquiryResponseDTO.InquiryListStructureForAdmin::toInquiryListResponseDTO).toList();
        log.info(InquiryResponseDTO.toInquiryResponseList(inquiryPage, inquiryList).toString());
        model.addAttribute("inquiryList", InquiryResponseDTO.toInquiryResponseList(inquiryPage, inquiryList));
        return "inquiry/inquiry-manage";
    }

    @GetMapping("/{inquiryId}")
    public String inquiryDetailPage(Model model , @PathVariable Integer inquiryId){
        model.addAttribute("inquiry", inquiryRepository.findByInquiryId(inquiryId));
        return "inquiry/inquiry-detail";
    }

    @PostMapping()
    public String inquiryAnswer(@ModelAttribute("inquiry") Inquiry inquiry){
        log.info(inquiry.toString());
        Inquiry target = inquiryRepository.findByInquiryId(inquiry.getInquiryId());
        target.update(inquiry.getInquiryAnswer());
        inquiryRepository.save(target);
        return "redirect:/admin/inquiry/list/0";
    }




}
