package com.f2z.gach.AI;

import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.DataGetter.dataEntity;
import com.f2z.gach.DataGetter.dataRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/ai")
public class AdminAIController {

    private final AdminRepository adminRepository;
    private final InquiryRepository inquiryRepository;
    private  final  AIService aiService;
    private final dataRepository dataRepo;

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

    @GetMapping("/{min}/{max}/{mapping}")
    @ResponseBody
    public String list(@PathVariable int min, @PathVariable int max, @PathVariable boolean mapping) {
        log.info("호출");
        return aiService.filterData(min, max, mapping).toString();
    }


    @GetMapping("/test")
    public void test(String[] args) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("/opt/homebrew/opt/python@3.12/bin/python3.12", "lstm.py", "1234");
        pb.redirectErrorStream(true);
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder sb = new StringBuilder();
        sb.append(reader.readLine());
        reader.close();

        log.info(sb.toString());

        process.destroy();

        /**
         * 1. 학습 코드를 실행시키는 방법
         * 2. 재학습 코드를 실행시키는 방법
         * 3. 현재 UserHistory에서 필터링을 진행하는 방법
         *
         * 1. 필터링을 어떻게 시킬 것인가?
         * userHistory에 있는 데이터를 SpringBoot에서 메소드를 통해 필터링 시켜서 이후의 이전의 개수 이후의 개수를 출력하고, .csv 파일로 저장한다.
         * 2. LSTM 모델을 만든다.
         * 3. 모델을 python에서 load()하고, input data를 집어넣고, 아웃풋 결과를 확인한다.
         */
    }
}
