package com.f2z.gach.AI;

import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.DataGetter.dataRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.History.Repository.HistoryLineTimeRepository;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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
    final String modelPath = "/home/t24102/GachProject/AI/Model";

    @ModelAttribute
    public void setAttributes(Model model){
        model.addAttribute("waiterListSize", adminRepository.findByAdminAuthorization(Authorization.WAITER).size());
        model.addAttribute("inquiryWaitSize", inquiryRepository.countByInquiryProgressIsFalse());
    }

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("aiList", aiRepo.findAll());
        return "ai/ai-manage";
    }

    @GetMapping("/model/add")
    public String addModel(Model model) {
        model.addAttribute("dataListLength", dataRepo.count() + lineTimeRepository.count());
        return "ai/ai-add";
    }

    @GetMapping("/model/add/filter/{min}/{max}")
    @ResponseBody
    public int getFilterNum(@PathVariable int min, @PathVariable int max){
        return aiService.filterData(min, max);
    }

    @GetMapping("/model/add/{hidden}/{epochs}/{rate}/{layer}/{batchSize}")
    @ResponseBody
    public String getFilterNum(@PathVariable int epochs, @PathVariable int layer, @PathVariable int hidden,
                            @PathVariable double rate, @PathVariable int batchSize) throws Exception {
        String value = aiService.makeModel(hidden, epochs, layer, rate, batchSize);
        return value;
    }

    @PostMapping("/model/add")
    @ResponseBody
    public int saveModel(@RequestBody ModelRequestDTO dto) {
        AiModel aiModel = AiModel.setToDto(dto);
        aiModel.setAverSatis(0);
        aiService.saveModel(dto.getModelName());
        aiModel.setAiModelPath(modelPath + "/" + dto.getModelName() + ".pt");
        aiRepo.save(aiModel);
        return 1;
    }
}
