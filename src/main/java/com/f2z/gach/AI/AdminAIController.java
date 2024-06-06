package com.f2z.gach.AI;

import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.DataGetter.dataRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.History.Repository.HistoryLineTimeRepository;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    String modelPath = "/home/t24102/AI/model/";


    @ModelAttribute
    public void setAttributes(Model model){
        model.addAttribute("waiterListSize", adminRepository.findByAdminAuthorization(Authorization.WAITER).size());
        model.addAttribute("inquiryWaitSize", inquiryRepository.countByInquiryProgressIsFalse());
    }

    // List
    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GUEST')")
    public String list(Model model) {
        model.addAttribute("aiList", aiRepo.findAll());
        return "ai/ai-manage";
    }

    @GetMapping("/check/{modelId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String checkModel(@PathVariable int modelId) {

        aiRepo.findAll().forEach(aiModel -> {
            aiModel.setIsChecked(false);
            aiRepo.save(aiModel);
        });
        AiModel aiModel = aiRepo.findById(modelId).orElseThrow();
        aiModel.setIsChecked(true);
        aiRepo.save(aiModel);
        return "redirect:/admin/ai";
    }

    // 뷰
    @GetMapping("/model/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addModel(Model model) {
        dataLength = lineTimeRepository.count() + dataRepo.count() - aiRepo.findAiModelWithMaxId().orElseThrow().getDataLength();
        model.addAttribute("dataListLength", dataLength);
        return "ai/ai-add";
    }

    // 뷰
    @GetMapping("/model/new/learn")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addNewModel(Model model) {
        dataLength = lineTimeRepository.count() + dataRepo.count();
        model.addAttribute("dataListLength", dataLength);
        return "ai/ai-newAdd";
    }

    // 필터링
    @GetMapping("/model/add/filter/new/{min}/{max}/{augment}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public long getNewFilterNum(@PathVariable int min, @PathVariable int max, @PathVariable int augment){
        log.info(String.valueOf(dataLength));
        return aiService.allFilterAndAugmentData(min, max, augment);
    }

    // 필터링
    @GetMapping("/model/add/filter/{min}/{max}/{augment}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public long getFilterNum(@PathVariable int min, @PathVariable int max, @PathVariable int augment){
        log.info(String.valueOf(dataLength));
        return aiService.filterAndAugmentData(min, max, augment, lineTimeRepository.count() + dataRepo.count() - aiRepo.findAiModelWithMaxId().orElseThrow().getDataLength());
    }

    // AI모델 재학습 기능
    @GetMapping("/model/learn/{name}/{version}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public int learningModel(@PathVariable String name, @PathVariable String version) throws Exception {
        String tempPath = modelPath + name + ".pkl";
        dataLength = lineTimeRepository.count() + dataRepo.count();
        AiModel aiModel = new AiModel();
        AiModel beforeModel = aiRepo.findAiModelWithMaxId().orElseThrow();
        aiModel.setAiModelPath(tempPath);
        aiModel.setAiModelName(name);
        aiModel.setAiModelVersion(version);
        aiModel.setMaxFeature(beforeModel.getMaxFeature());
        aiModel.setMinSampleLeaf(beforeModel.getMinSampleLeaf());
        aiModel.setMinSampleSplit(beforeModel.getMinSampleSplit());
        aiModel.setMaxDepth(beforeModel.getMaxDepth());
        aiModel.setIsChecked(false);
        aiModel.setDataLength(dataLength);
        aiModel.setMse(aiService.reLearnModel(aiModel));
        aiRepo.save(aiModel);
        return 1;
    }

    // AI모델 학습 기능
    @GetMapping("/model/learn/new/{name}/{version}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public List<Integer> learningNewModel(@PathVariable String name, @PathVariable String version) throws Exception {
        String tempPath = modelPath + name + ".pkl";
        dataLength = lineTimeRepository.count() + dataRepo.count();
        AiModel aiModel = new AiModel();
        AiModel beforeModel = aiRepo.findAiModelWithMaxId().orElseThrow();
        aiModel.setAiModelPath(tempPath);
        aiModel.setAiModelName(name);
        aiModel.setAiModelVersion(version);
        aiModel.setMaxFeature(beforeModel.getMaxFeature());
        aiModel.setMinSampleLeaf(beforeModel.getMinSampleLeaf());
        aiModel.setMinSampleSplit(beforeModel.getMinSampleSplit());
        aiModel.setMaxDepth(beforeModel.getMaxDepth());
        aiModel.setIsChecked(false);
        aiModel.setDataLength(dataLength);
        List<Integer> arrayList = aiService.learnModel(aiModel);
        List<Integer> arrayListClone = new ArrayList<>(arrayList);
        aiModel.setMse(aiService.getMae());
        aiModel.setMaxDepth(arrayList.get(2));
        aiModel.setMinSampleSplit(arrayList.get(0));
        aiModel.setMinSampleLeaf(arrayList.get(1));
        aiModel.setAccuracy(getAccuracy(arrayListClone));
        aiRepo.save(aiModel);
        return arrayList;
    }

    double getAccuracy(List<Integer> arrClone){
        arrClone.remove(0);
        arrClone.remove(1);
        arrClone.remove(2);

        List<Integer> real = new ArrayList<>();
        List<Integer> pred = new ArrayList<>();
        List<Double> accuracy = new ArrayList<>();
        for(int i = 0; i < arrClone.size(); i++){
            if(i < arrClone.size()/2){
                real.add(arrClone.get(i));
            } else {
                pred.add(arrClone.get(i));
            }
        }

        for(int i = 0; i < real.size(); i++){

            if(Math.abs((double) real.get(i)) > Math.abs((double) pred.get(i))){
                accuracy.add(
                        1.0 - ( (Math.abs((double) real.get(i) - (double) pred.get(i))) / Math.abs((double) real.get(i)) )
                );
            } else {
                accuracy.add(
                        1.0 - ( (Math.abs((double) pred.get(i) - (double) real.get(i))) / Math.abs((double) pred.get(i)) )
                );
            }
        }

        log.info(String.valueOf(real.size()));
        log.info(String.valueOf(pred.size()));
        accuracy.forEach(i -> log.info(String.valueOf(i)));
        return 0.0;
    }
}
