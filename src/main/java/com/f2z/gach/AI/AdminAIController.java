package com.f2z.gach.AI;

import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.DataGetter.dataEntity;
import com.f2z.gach.DataGetter.dataRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.History.Repository.HistoryLineTimeRepository;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/ai")
public class AdminAIController {

    private final AdminRepository adminRepository;
    private final InquiryRepository inquiryRepository;
    private final HistoryLineTimeRepository lineTimeRepository;
    private final dataRepository dataRepo;
    private final AIService aiService;
    private final AiModelRepository aiRepo;
    long dataLength;
    final String modelPath = "/home/t24102/GachProject/AI/Model";
//    final String modelPath = "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Model";

    @ModelAttribute
    public void setAttributes(Model model){
        model.addAttribute("waiterListSize", adminRepository.findByAdminAuthorization(Authorization.WAITER).size());
        model.addAttribute("inquiryWaitSize", inquiryRepository.countByInquiryProgressIsFalse());
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GUEST')")
    public String list(Model model) {
        model.addAttribute("aiList", aiRepo.findAll());
        return "ai/ai-manage";
    }

    // 뷰
    @GetMapping("/model/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addModel(Model model) {
        dataLength = lineTimeRepository.count();
        model.addAttribute("dataListLength", dataLength);
        return "ai/ai-add";
    }

    // 필터링
    @GetMapping("/model/add/filter/{min}/{max}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public long getFilterNum(@PathVariable int min, @PathVariable int max){
        dataLength = aiService.filterData(min, max);
        return dataLength;
    }

    @GetMapping("/model/learn")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public int learningModel() throws Exception {
        aiService.reLearnModel();
        return 1;
    }

    // 모델 추가 작업
    @PostMapping("/model/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public int saveModel(@RequestBody ModelRequestDTO dto) {
        dataLength = dataRepo.count() + lineTimeRepository.count();
        AiModel aiModel = AiModel.setToDto(dto);
        aiModel.setAverSatis(0);
        aiModel.setLastDataIndex(lineTimeRepository.count());
        aiService.saveModel(dto.getModelName());
        aiModel.setAiModelPath(modelPath + "/" + dto.getModelName() + ".pkl");
        aiRepo.save(aiModel);
        return 1;
    }

    @GetMapping("/delete/model/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteModel(@PathVariable int id) {
        AiModel aiModel = aiRepo.findById(id).orElseThrow();
        aiService.deleteModel(aiModel);
        aiRepo.delete(aiModel);
        return "redirect:/admin/ai";
    }
}
