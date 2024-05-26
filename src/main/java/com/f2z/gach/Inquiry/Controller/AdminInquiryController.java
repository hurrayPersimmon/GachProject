package com.f2z.gach.Inquiry.Controller;

import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.Inquiry.DTO.InquiryResponseDTO;
import com.f2z.gach.Inquiry.Entity.Inquiry;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GUEST')")
    public String inquiryListPage(Model model, @PathVariable Integer page){
        Pageable pageable = PageRequest.ofSize(10).withSort(Sort.Direction.DESC,"inquiryId").withPage(page);
        Page<Inquiry> inquiryPage = inquiryRepository.findAll(pageable);
        List<InquiryResponseDTO.InquiryListStructureForAdmin> inquiryList = inquiryPage.getContent().stream()
                .map(InquiryResponseDTO.InquiryListStructureForAdmin::toInquiryListResponseDTO).toList();
        log.info(InquiryResponseDTO.toInquiryResponseList(inquiryPage, inquiryList).toString());
        model.addAttribute("inquiryList", InquiryResponseDTO.toInquiryResponseList(inquiryPage, inquiryList));
        model.addAttribute("inquiryChartData", inquiryRepository.findAll());
        return "inquiry/inquiry-manage";
    }

    @GetMapping("/sortedlist/{page}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GUEST')")
    public String inquiryListSortedPage(Model model, @PathVariable Integer page, @RequestParam String sort){
        Pageable pageable = PageRequest.ofSize(10).withSort(Sort.Direction.DESC, "inquiryId").withPage(page);
        Page<Inquiry> inquiryPage = inquiryRepository.findAllByInquiryTitleContaining(sort, pageable);
        List<InquiryResponseDTO.InquiryListStructureForAdmin> inquiryList = inquiryPage.getContent().stream()
                .map(InquiryResponseDTO.InquiryListStructureForAdmin::toInquiryListResponseDTO).toList();
        model.addAttribute("inquiryList", InquiryResponseDTO.toInquiryResponseList(inquiryPage, inquiryList));
        model.addAttribute("inquiryChartData", inquiryRepository.findAll());
        return "inquiry/inquiry-manage";
    }

    @GetMapping("/required/{page}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GUEST')")
    public String inquiryRequiredPage(Model model, @PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "inquiryId");
        Page<Inquiry> inquiryPage = inquiryRepository.findByInquiryProgressFalse(pageable);
        List<InquiryResponseDTO.InquiryListStructureForAdmin> inquiryList = inquiryPage.getContent().stream()
                .map(InquiryResponseDTO.InquiryListStructureForAdmin::toInquiryListResponseDTO).toList();
        log.info(InquiryResponseDTO.toInquiryResponseList(inquiryPage, inquiryList).toString());
        model.addAttribute("inquiryList", InquiryResponseDTO.toInquiryResponseList(inquiryPage, inquiryList));
        return "inquiry/inquiry-required";
    }

    @GetMapping("/{inquiryId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GUEST')")
    public String inquiryDetailPage(Model model , @PathVariable Integer inquiryId){
        model.addAttribute("inquiry", inquiryRepository.findByInquiryId(inquiryId));
        log.info(inquiryRepository.findByInquiryId(inquiryId).toString());
        return "inquiry/inquiry-detail";
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String inquiryAnswer(@ModelAttribute Inquiry inquiry){
        log.info(inquiry.toString());
        Inquiry target = inquiryRepository.findByInquiryId(inquiry.getInquiryId());
        target.update(inquiry.getInquiryAnswer());
        inquiryRepository.save(target);
        return "redirect:/admin/inquiry/list/0";
    }
}
