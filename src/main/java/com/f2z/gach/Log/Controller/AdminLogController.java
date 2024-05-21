package com.f2z.gach.Log.Controller;

import com.f2z.gach.Log.Repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminLogController {
    private final LogRepository logRepository;

    @GetMapping("/log/manage")
    public String manage(Model model) {
        model.addAttribute("logs", logRepository.findAll());
        return "log/log-manage";
    }
}
