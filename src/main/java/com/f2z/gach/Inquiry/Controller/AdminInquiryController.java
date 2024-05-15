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
        log.info(inquiryRepository.findByInquiryId(inquiryId).toString());
        //Inquiry(inquiryId=2, user=User(userId=569153915064443390, username=wwj0313, password=7cf6b48442be8445a03acd315b70349dddbfdcef7b4eb680e71b26993def1519,
        // userNickname=웅쥬쥬, userSpeed=NORMAL, userGender=남, userBirth=2000, userHeight=173.0, userWeight=73.0), inquiryProgress=false, inquiryTitle=테스트,
        // inquiryContent=테스트입니다., inquiryAnswer=null, inquiryCategory=Node)
        return "inquiry/inquiry-detail";
    }

    @PostMapping()
    public String inquiryAnswer(@ModelAttribute("inquiry") Inquiry inquiry){
        //formData
        //inquiryId: 2
        //inquiryCategory: Node
        //user.userNickname: 웅쥬쥬
        //createDt: 24. 5. 8. 오후 11:06
        //inquiryTitle: 테스트
        //inquiryContent: 테스트입니다.
        //inquiryAnswer: 그래그래요.
        //TODO: 제목, 내용, 닉네임, 시간은 수정하지 못하도록 해야할 듯.
        Inquiry target = inquiryRepository.findByInquiryId(inquiry.getInquiryId());
        //update 메소드가 inquiryProgress를 true로 만듦.
        target.update(inquiry.getInquiryAnswer());
        inquiryRepository.save(target);
        return "redirect:/admin/inquiry/list/0";
    }
}
