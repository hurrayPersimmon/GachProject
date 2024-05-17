package com.f2z.gach.AI;

import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.DataGetter.dataEntity;
import com.f2z.gach.DataGetter.dataRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.EnumType.Gender;
import com.f2z.gach.EnumType.Satisfaction;
import com.f2z.gach.EnumType.Speed;
import com.f2z.gach.History.Entity.UserHistory;
import com.f2z.gach.History.Repository.UserHistoryRepository;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import com.f2z.gach.Map.Repository.MapNodeRepository;
import com.f2z.gach.User.Entity.User;
import com.f2z.gach.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/ai")
public class AdminAIController {

    private final AdminRepository adminRepository;
    private final InquiryRepository inquiryRepository;
    private final dataRepository dataRepo;
    private final AIService aiService;

    @ModelAttribute
    public void setAttributes(Model model){
        model.addAttribute("waiterListSize", adminRepository.findByAdminAuthorization(Authorization.WAITER).size());
        model.addAttribute("inquiryWaitSize", inquiryRepository.countByInquiryProgressIsFalse());
    }

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("dataListLength", dataRepo.count());
        return "ai/ai-manage";
    }

    @GetMapping("/model/add")
    public String addModel(Model model) {
        model.addAttribute("dataListLength", dataRepo.count());
        return "ai/ai-add";
    }

    @GetMapping("/model/add/filter/{min}/{max}")
    @ResponseBody
    public int getFilterNum(@PathVariable int min, @PathVariable int max){
        return aiService.filterData(min, max);
    }

    @GetMapping("/model/add/{epochs}/{layer}/{rate}/{hidden}")
    @ResponseBody
    public int getFilterNum(@PathVariable int epochs, @PathVariable int layer, @PathVariable int hidden, @PathVariable double rate) throws Exception {
        return aiService.makeModel(hidden, epochs, layer, rate);
    }
    // 각각의 Line을 쪼개서 나한테 보내줘야함.

}
