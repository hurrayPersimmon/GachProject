package com.f2z.gach.AI;

import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.DataGetter.dataEntity;
import com.f2z.gach.DataGetter.dataRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.History.Repository.HistoryLineTimeRepository;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    final String localModelPath = "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Model";

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
        dataLength = dataRepo.count() + lineTimeRepository.count();
        model.addAttribute("dataListLength", dataLength);
        return "ai/ai-add";
    }

    @GetMapping("/model/add/filter/{min}/{max}")
    @ResponseBody
    public long getFilterNum(@PathVariable int min, @PathVariable int max){
        dataLength = aiService.filterData(min, max);
        return dataLength;
    }

    @GetMapping("/model/re/filter/{modelId}/{min}/{max}")
    @ResponseBody
    public long getFilterReNum(@PathVariable int modelId, @PathVariable int min, @PathVariable int max){
        AiModel aiModel = aiRepo.findById(modelId).orElseThrow();
        List<dataEntity> dataEntityList = dataRepo.findByDataIdBetween(aiModel.getDataLength()-dataRepo.count(), lineTimeRepository.count());
        dataLength = aiService.filterData(min, max, dataEntityList);
        return dataLength;
    }

    @GetMapping("/model/add/{hidden}/{epochs}/{rate}/{layer}/{batchSize}")
    @ResponseBody
    public String getLearnNum(@PathVariable int epochs, @PathVariable int layer, @PathVariable int hidden,
                            @PathVariable double rate, @PathVariable int batchSize) throws Exception {
        String str = aiService.makeModel(hidden, epochs, layer, rate, batchSize);
        return str;
    }

    @GetMapping("/model/add/{epochs}/{rate}/{modelId}")
    @ResponseBody
    public String getReLearnNum(@PathVariable int epochs, @PathVariable double rate, @PathVariable int modelId) throws Exception {
        AiModel aiModel = aiRepo.findById(modelId).orElseThrow();
        String str = aiService.doMakeModel(epochs, rate,aiModel.getAiModelPath());
        return str;
    }

    @PostMapping("/model/add")
    @ResponseBody
    public int saveModel(@RequestBody ModelRequestDTO dto) {
        dataLength = dataRepo.count() + lineTimeRepository.count();
        AiModel aiModel = AiModel.setToDto(dto);
        aiModel.setAverSatis(0);
        aiModel.setDataLength(dataLength);
        aiModel.setLastDataIndex(lineTimeRepository.count());
        aiService.saveModel(dto.getModelName());
        aiModel.setAiModelPath(modelPath + "/" + dto.getModelName() + ".pt");
        aiRepo.save(aiModel);
        return 1;
    }

    @GetMapping("/model/{id}")
    public String getLearnNum(@PathVariable int id, Model model) {
        AiModel aiModel = aiRepo.findById(id).orElseThrow();
        dataLength = dataRepo.count() + lineTimeRepository.count();
        long additionalDataLength = dataLength - aiModel.getDataLength();
        model.addAttribute("aiModel", aiModel);
        model.addAttribute("additionalDataLength", additionalDataLength);
        return "ai/ai-detail";
    }

    @GetMapping("/delete/model/{id}")
    public String deleteModel(@PathVariable int id, Model model) {
        AiModel aiModel = aiRepo.findById(id).orElseThrow();
        aiService.deleteModel(aiModel);
        aiRepo.delete(aiModel);

        return "redirect:/admin/ai";
    }
}
