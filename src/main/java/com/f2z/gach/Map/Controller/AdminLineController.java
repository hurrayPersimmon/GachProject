package com.f2z.gach.Map.Controller;

import com.f2z.gach.DTO.Map.MapLineDTO;
import com.f2z.gach.Map.DTO.MapDTO;
import com.f2z.gach.Map.Entity.MapNode;
import com.f2z.gach.Map.Repository.MapLineRepository;
import com.f2z.gach.Map.Repository.MapNodeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@SessionAttributes
public class AdminLineController {
    private final MapLineRepository mapLineRepository;
    private final MapNodeRepository mapNodeRepository;

    @GetMapping("/line")
    public String lineListPage(Model model){
        model.addAttribute("lineList", mapLineRepository.findAll());
        return "line-manage";
    }

    @GetMapping("/line/add")
    public String addLinePage(Model model){
        model.addAttribute("lineDto", new MapLineDTO());
        model.addAttribute("nodeList", mapNodeRepository.findAll());
        return "line-add";
    }

    @PostMapping("/line")
    public String addLine(@Valid @ModelAttribute("lineDto") MapDTO.MapLineDTO mapLineDTO,
                          BindingResult result){
        if(result.hasErrors()){
            return "line-add";
        }
        log.info(mapLineDTO.toString());
        mapLineRepository.save(mapLineDTO.toSaveEntity("A",
                mapNodeRepository.findByNodeName(mapLineDTO.toEntity().getNodeNameFirst()),
                mapNodeRepository.findByNodeName(mapLineDTO.toEntity().getNodeNameSecond())));

        mapLineRepository.save(mapLineDTO.toSaveEntity("B",
                mapNodeRepository.findByNodeName(mapLineDTO.toEntity().getNodeNameSecond()),
                mapNodeRepository.findByNodeName(mapLineDTO.toEntity().getNodeNameFirst())));
        log.info("성공");
        return "redirect:/admin/line";
    }

    @GetMapping("/line/{lineId}")
    public String deleteLine(@PathVariable Integer lineId,
                             Model model)throws Exception{
        if(mapLineRepository.existsById(lineId)){
            mapLineRepository.deleteById(lineId);
            if(lineId%2==0) lineId-=1;
            else lineId+=1;
            mapLineRepository.deleteById(lineId);
            model.addAttribute("message", "간선이 삭제되었습니다.");
            model.addAttribute("lineList", mapLineRepository.findAll());
            return "redirect:/admin/line";
        }
        throw new Exception();
    }
}
