package com.f2z.gach.Map.Controller;

import com.f2z.gach.DTO.Map.MapLineDTO;
import com.f2z.gach.Map.Repository.MapLineRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@SessionAttributes
public class AdminLineController {
    private final MapLineRepository mapLineRepository;

    @GetMapping("/line")
    public String lineListPage(Model model){
        model.addAttribute("lineList", mapLineRepository.findAll());
        return "line-manage";
    }

    @GetMapping("/line/add")
    public String addLinePage(Model model){
        model.addAttribute("lineDto", new MapLineDTO());
        return "line-add";
    }

    @PostMapping("/line")
    public String addLine(@Valid @ModelAttribute("lineDto") MapLineDTO mapLineDTO,
                          BindingResult result,
                          Model model){
        if(result.hasErrors()){
            return "line-add";
        }else if(mapLineRepository.existsByLineName(mapLineDTO.toEntity().getLineName())){
            model.addAttribute("errorMessage", "이미 존재하는 노드입니다.");
            return "line-add";
        }

        mapLineRepository.save(mapLineDTO.toEntity());
        model.addAttribute("message", "노드가 추가되었습니다.");
        model.addAttribute("lineList", mapLineRepository.findAll());
        return "line-manage";
    }


}
